package com.example.central_node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
public class CentralNodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralNodeApplication.class, args);
	}

}
