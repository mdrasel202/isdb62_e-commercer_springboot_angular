package com.rasel.security_demo;

import com.rasel.security_demo.Service.FileStorageService;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurityDemoApplication implements CommandLineRunner {

	@Resource
	FileStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(SecurityDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.init();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
