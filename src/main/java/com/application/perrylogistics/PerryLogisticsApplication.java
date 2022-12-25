package com.application.perrylogistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class PerryLogisticsApplication {

	public static void main(String[] args) {

		SpringApplication.run(PerryLogisticsApplication.class, args);
	}

}
