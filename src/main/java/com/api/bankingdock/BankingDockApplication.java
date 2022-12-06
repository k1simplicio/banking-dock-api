package com.api.bankingdock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BankingDockApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingDockApplication.class, args);
	}
	@GetMapping("/")
	public String index(){
		return "Nothing to see here";
	}
}
