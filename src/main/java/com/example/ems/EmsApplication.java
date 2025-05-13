package com.example.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class EmsApplication {

	public static void main(String[] args) {

//		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		SpringApplication.run(EmsApplication.class, args);
//		System.out.println("Current TimeZone: " + TimeZone.getDefault().getID());
	}

}