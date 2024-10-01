	package com.example.central_node;

	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.autoconfigure.domain.EntityScan;
	import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
	import org.springframework.scheduling.annotation.EnableAsync;
	import org.springframework.scheduling.annotation.EnableScheduling;

	@SpringBootApplication
	@EnableAsync
	@EnableJpaRepositories(basePackages = "com.example.central_node.repository")
	@EntityScan(basePackages = "com.example.central_node.model")
	public class CentralNodeApplication {

		public static void main(String[] args) {
			SpringApplication.run(CentralNodeApplication.class, args);
		}

	}
