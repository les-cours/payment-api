package com.arini.paiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class PaimentApplication {

	@RequestMapping("/")
	public String home() {
		return "Dockerizing Spring Boot Application";
	}


	public static void main(String[] args) {
		SpringApplication.run(PaimentApplication.class, args);
	}

}
