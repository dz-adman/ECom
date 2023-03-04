package com.ad.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableCaching
// statemachine packages explicitly mentioned here as all hte beans were not being scanned
@EnableJpaRepositories({"org.springframework.statemachine.data.jpa", "com.ad.ecom.*"})
@EntityScan({"org.springframework.statemachine.data.jpa", "com.ad.ecom.*"})
public class EComBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EComBootApplication.class, args);
	}

}
