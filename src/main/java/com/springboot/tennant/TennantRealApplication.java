package com.springboot.tennant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TennantRealApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennantRealApplication.class, args);
	}

}
