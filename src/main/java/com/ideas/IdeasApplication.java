package com.ideas;

import com.ideas.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdeasApplication extends SwaggerConfig {

	public static void main(String[] args) {
		SpringApplication.run(IdeasApplication.class, args);
	}

}
