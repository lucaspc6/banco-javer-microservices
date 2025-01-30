package com.example.clienteAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClienteApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteApiApplication.class, args);
		
	}

}