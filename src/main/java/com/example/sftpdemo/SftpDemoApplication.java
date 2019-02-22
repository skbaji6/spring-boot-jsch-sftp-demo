package com.example.sftpdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jcraft.jsch.JSch;

@SpringBootApplication
@EnableScheduling
public class SftpDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SftpDemoApplication.class, args);
	}

	@Bean
	public JSch jsch() {
		return new JSch();
	}

}
