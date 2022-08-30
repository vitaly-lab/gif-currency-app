package com.gifcurrencyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GifCurrencyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(GifCurrencyAppApplication.class, args);
	}

}
