package com.ab.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.ab")
@SpringBootApplication
public class CurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyApplication.class, args);
	}

}
