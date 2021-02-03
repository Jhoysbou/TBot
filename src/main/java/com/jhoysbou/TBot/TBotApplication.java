package com.jhoysbou.TBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:login.properties")
public class TBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TBotApplication.class, args);
	}

}
