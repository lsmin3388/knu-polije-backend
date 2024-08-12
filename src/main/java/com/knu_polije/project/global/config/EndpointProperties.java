package com.knu_polije.project.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "endpoints")
public class EndpointProperties {
	private String breedDetect;
	private String weightDetect;
	private String miniatureWeightDetect;
}
