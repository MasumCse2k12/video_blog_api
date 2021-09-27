package com.rokomari.videoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class VideoApi extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(VideoApi.class, args);
	}

}
