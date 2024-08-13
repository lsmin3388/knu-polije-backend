import os
import sys
import time
import base64
from pathlib import Path
import cv2
import torch
import numpy as np
import yaml
from flask import Flask, request, jsonify, send_from_directory

# Append yolov7 to sys.path to import modules
sys.path.append(os.path.join(os.path.dirname(__file__), 'yolov7'))

from yolov7.utils.plots import plot_one_box
from yolov7.models.experimental import attempt_load
from yolov7.utils.datasets import letterbox
from yolov7.utils.general import check_img_size, non_max_suppression, scale_coords, xyxy2xywh
from yolov7.utils.torch_utils import select_device, time_synchronized

# Define the CattleWeightPredictor class
class CattleWeightPredictor:
    def __init__(self, model_path):
        self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
        self.model = self.load_model(model_path).to(self.device)
        self.names = ['back-leg', 'body-side', 'front-leg', 'head']
        self.colors = [(0, 0, 255), (0, 255, 0), (255, 0, 0), (255, 255, 0)]  # BGR colors for each class
        self.total_weight = 0.0

    def load_model(self, model_path):
        model = attempt_load(model_path, map_location=self.device)
        model.eval()
        return model

    def predict(self, image, conf_thres=0.25, iou_thres=0.45):  # Add thresholds as parameters
        img0 = image.copy()  # BGR image

        # Preprocess the image
        img = letterbox(img0, new_shape=640)[0]
        img = img.astype('float32')  # Convert to float32
        img /= 255.0  # Normalize 0-255 to 0.0-1.0
        img = torch.from_numpy(img).permute(2, 0, 1).unsqueeze(0).to(self.device)  # To tensor

        with torch.no_grad():
            pred = self.model(img)[0]
            # Use the passed thresholds for NMS
            pred = non_max_suppression(pred, conf_thres=conf_thres, iou_thres=iou_thres)

        results = []
        self.total_weight = 0.0  # Reset total weight
        for det in pred:
            if det is not None and len(det):
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], img0.shape).round()
                for *xyxy, conf, cls in reversed(det):
                    class_idx = int(cls.item())
                    weight = self.calculate_weight(class_idx, conf.item())
                    results.append({
                        "class": class_idx,
                        "class_name": self.names[class_idx],
                        "weight": weight,
                        "bbox": [int(coord) for coord in xyxy]  # Ensure bbox is JSON serializable
                    })
                    self.total_weight += weight  # Add weight to total

                    # Draw bounding box
                    label = f'{self.names[class_idx]} {weight:.2f} kg'
                    plot_one_box(xyxy, img0, label=label, color=self.colors[class_idx])

        return results, self.total_weight, img0

    def calculate_weight(self, class_idx, confidence, is_miniature=False):
        # Implement your weight calculation logic based on class_idx and confidence
        # Example logic based on class_idx and confidence
        weight_factors = [0.05, 0.1, 0.05, 0.07]  # Example factors for back-leg, body-side, front-leg, head
        weight = confidence * weight_factors[class_idx] * 1000  # Convert to grams and then kg

        if is_miniature:
            # Adjust weight for the miniature scale
            # Assuming the real cow is about 1.5 meters long on average
            real_cow_length = 150.0  # cm
            miniature_cow_length = 13.5  # cm
            scale_factor = real_cow_length / miniature_cow_length

            # Assuming the miniature cow weighs
            estimated_real_weight = 500 * scale_factor

            # Adjust weight by comparing it to a known average weight for miniature size
            weight = (weight / real_cow_length) * miniature_cow_length

        return weight

# Initialize Flask app
app = Flask(__name__)

# Configure directories
UPLOAD_FOLDER = 'uploads'
STATIC_FOLDER = 'static'

# Ensure directories exist
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
os.makedirs(STATIC_FOLDER, exist_ok=True)

# Load YOLOv7 model for cattle breed detection
# weights_breed = 'yolov7/best.pt'  # Path to breed model weights
weights_breed = 'yolov7/best.pt'  # Path to breed model weights
device = select_device('0' if torch.cuda.is_available() else 'cpu')
model_breed = attempt_load(weights_breed, map_location=device)  # Load FP32 model
img_size = 640
stride = int(model_breed.stride.max())  # Model stride
img_size = check_img_size(img_size, s=stride)  # Check img_size
model_breed.half() if device.type != 'cpu' else model_breed.float()  # to FP16 or FP32
model_breed.eval()  # Set model to evaluation mode

# Load YOLOv7 model for cattle weight estimation
weights_weight = 'yolov7/bestwgt2.pt'  # Path to weight model weights
predictor = CattleWeightPredictor(weights_weight)

# Warmup
model_breed(torch.zeros(1, 3, img_size, img_size).to(device).type_as(next(model_breed.parameters())))

# Get class names from the model for breed detection
names_breed = model_breed.module.names if hasattr(model_breed, 'module') else model_breed.names

@app.route('/predict_breed', methods=['POST'])
def predict_breed():
    if 'image' not in request.files:
        return jsonify({'error': 'No image provided'}), 400

    # Read image from request
    file = request.files['image']
    image_bytes = file.read()
    image = cv2.imdecode(np.frombuffer(image_bytes, np.uint8), cv2.IMREAD_COLOR)

    # Prepare image
    img_resized = letterbox(image, img_size, stride=stride, auto=True)[0]
    img_resized = img_resized[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, to 3x416x416
    img_resized = np.ascontiguousarray(img_resized)

    img_tensor = torch.from_numpy(img_resized).to(device)
    img_tensor = img_tensor.half() if device.type != 'cpu' else img_tensor.float()  # uint8 to fp16/32
    img_tensor /= 255.0  # 0 - 255 to 0.0 - 1.0
    if img_tensor.ndimension() == 3:
        img_tensor = img_tensor.unsqueeze(0)

    # Inference
    t1 = time_synchronized()
    with torch.no_grad():
        pred = model_breed(img_tensor, augment=False)[0]
    t2 = time_synchronized()

    # Apply NMS
    conf_thres = 0.2  # Make sure this matches the CLI
    iou_thres = 0.1  # Match this as well
    pred = non_max_suppression(pred, conf_thres, iou_thres, classes=None, agnostic=False)
    t3 = time_synchronized()

    # Process detections
    results = []
    for i, det in enumerate(pred):  # detections per image
        if len(det):
            det[:, :4] = scale_coords(img_tensor.shape[2:], det[:, :4], image.shape).round()
            for *xyxy, conf, cls in reversed(det):
                label_name = names_breed[int(cls)]
                results.append({'label': label_name})
                # Draw bounding box
                plot_one_box(xyxy, image, label=f'{label_name} {conf:.2f}', color=(0, 255, 0), line_thickness=2)

    # Save the image with bounding boxes
    output_image_path = os.path.join(STATIC_FOLDER, 'output_with_boxes.jpg')
    cv2.imwrite(output_image_path, image)

    # Provide a direct URL to the image
    processed_image_url = request.url_root + 'static/output_with_boxes.jpg'

    response_data = {
        'results': results,
        'image_url': processed_image_url  # Include the image URL in the JSON response
    }

    print(f'Done. Inference time: {(t2 - t1):.3f}s, NMS time: {(t3 - t2):.3f}s')

    return jsonify(response_data)

@app.route('/predict_weight', methods=['POST'])
def predict_weight():
    if 'image' not in request.files:
        return jsonify({'error': 'No image provided'}), 400

    # Read image from request
    file = request.files['image']
    image_bytes = file.read()
    image = cv2.imdecode(np.frombuffer(image_bytes, np.uint8), cv2.IMREAD_COLOR)

    # Define thresholds
    conf_threshold = 0.25
    iou_threshold = 0.45

    # Preprocess the image
    img_resized = letterbox(image, new_shape=640, auto=False)[0]  # Resize and pad image
    img_resized = img_resized[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, and HWC to CHW
    img_resized = np.ascontiguousarray(img_resized)

    img_tensor = torch.from_numpy(img_resized).to(predictor.device)
    img_tensor = img_tensor.half() if predictor.device.type != 'cpu' else img_tensor.float()  # Convert to fp16 or fp32
    img_tensor /= 255.0  # Normalize 0-255 to 0.0-1.0

    if img_tensor.ndimension() == 3:
        img_tensor = img_tensor.unsqueeze(0)  # Add batch dimension

    # Inference
    with torch.no_grad():
        pred = predictor.model(img_tensor)[0]
        # Use the passed thresholds for NMS
        pred = non_max_suppression(pred, conf_thres=conf_threshold, iou_thres=iou_threshold)

    # Process detections
    results = []
    total_weight = 0.0
    for det in pred:
        if det is not None and len(det):
            det[:, :4] = scale_coords(img_tensor.shape[2:], det[:, :4], image.shape).round()
            for *xyxy, conf, cls in reversed(det):
                class_idx = int(cls.item())
                weight = predictor.calculate_weight(class_idx, conf.item())
                results.append({
                    "class": class_idx,
                    "class_name": predictor.names[class_idx],
                    "weight": weight,
                    "bbox": [int(coord) for coord in xyxy]  # Ensure bbox is JSON serializable
                })
                total_weight += weight  # Add weight to total

                # Draw bounding box
                label = f'{predictor.names[class_idx]} {weight:.2f} kg'
                plot_one_box(xyxy, image, label=label, color=predictor.colors[class_idx])

    # Save the annotated image with bounding boxes
    output_image_path = os.path.join(STATIC_FOLDER, 'output_with_boxes_wght.jpg')
    cv2.imwrite(output_image_path, image)

    # Provide a direct URL to the image
    processed_image_url = request.url_root + 'static/output_with_boxes_wght.jpg'

    response_data = {
        'results': [{'label': r['class_name'], 'weight': r['weight'], 'bbox': r['bbox']} for r in results],
        'total_weight': total_weight,
        'image_url': processed_image_url  # Include the image URL in the JSON response
    }

    print(jsonify(response_data))

    return jsonify(response_data)

@app.route('/predict_miniature_weight', methods=['POST'])
def predict_miniature_weight():
    if 'image' not in request.files:
        return jsonify({'error': 'No image provided'}), 400

    # Read image from request
    file = request.files['image']
    image_bytes = file.read()
    image = cv2.imdecode(np.frombuffer(image_bytes, np.uint8), cv2.IMREAD_COLOR)

    # Define thresholds
    conf_threshold = 0.25
    iou_threshold = 0.45

    # Preprocess the image
    img_resized = letterbox(image, new_shape=640, auto=False)[0]  # Resize and pad image
    img_resized = img_resized[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, and HWC to CHW
    img_resized = np.ascontiguousarray(img_resized)

    img_tensor = torch.from_numpy(img_resized).to(predictor.device)
    img_tensor = img_tensor.half() if predictor.device.type != 'cpu' else img_tensor.float()  # Convert to fp16 or fp32
    img_tensor /= 255.0  # Normalize 0-255 to 0.0-1.0

    if img_tensor.ndimension() == 3:
        img_tensor = img_tensor.unsqueeze(0)  # Add batch dimension

    # Inference
    with torch.no_grad():
        pred = predictor.model(img_tensor)[0]
        # Use the passed thresholds for NMS
        pred = non_max_suppression(pred, conf_thres=conf_threshold, iou_thres=iou_threshold)

    # Process detections
    results = []
    total_weight = 0.0
    for det in pred:
        if det is not None and len(det):
            det[:, :4] = scale_coords(img_tensor.shape[2:], det[:, :4], image.shape).round()
            for *xyxy, conf, cls in reversed(det):
                class_idx = int(cls.item())
                weight = predictor.calculate_weight(class_idx, conf.item(), is_miniature=True)
                results.append({
                    "class": class_idx,
                    "class_name": predictor.names[class_idx],
                    "weight": weight,
                    "bbox": [int(coord) for coord in xyxy]  # Ensure bbox is JSON serializable
                })
                total_weight += weight  # Add weight to total

                # Draw bounding box
                label = f'{predictor.names[class_idx]} {weight:.2f} g'
                plot_one_box(xyxy, image, label=label, color=predictor.colors[class_idx])

    # Save the annotated image with bounding boxes
    output_image_path = os.path.join(STATIC_FOLDER, 'output_with_boxes_miniature_wght.jpg')
    cv2.imwrite(output_image_path, image)

    # Provide a direct URL to the image
    processed_image_url = request.url_root + 'static/output_with_boxes_miniature_wght.jpg'

    response_data = {
        'results': [{'label': r['class_name'], 'weight': r['weight'], 'bbox': r['bbox']} for r in results],
        'total_weight': total_weight,
        'image_url': processed_image_url  # Include the image URL in the JSON response
    }

    print(jsonify(response_data))

    return jsonify(response_data)

@app.route('/static/<filename>')
def serve_image(filename):
    # Serve the image file from the static directory
    return send_from_directory(STATIC_FOLDER, filename)

# Run the Flask app
if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', port=49270)
