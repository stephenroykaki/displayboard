package com.aphc.displayboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DisplayBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisplayBoardApplication.class, args);
	}

	@Bean
	public RestTemplate getRT() {
		return new RestTemplate();
	}
}
