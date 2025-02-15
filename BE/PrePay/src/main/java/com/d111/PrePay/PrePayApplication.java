package com.d111.PrePay;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "https://i12d111.p.ssafy.io",description = "https"), @Server(url = "http://localhost:8080",description = "로컬")})
@SpringBootApplication
public class PrePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrePayApplication.class, args);
	}

}
