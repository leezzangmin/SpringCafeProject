package com.zzangmin.gesipan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GesipanApplication {

	public static void main(String[] args) {
		SpringApplication.run(GesipanApplication.class, args);
	}

}
