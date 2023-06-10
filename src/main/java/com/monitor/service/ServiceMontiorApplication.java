package com.monitor.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.monitor.service")
public class ServiceMontiorApplication {

	public static void main(String[] args) {
		//System.setProperty("java.io.tmpdir", "/tmp");
		SpringApplication.run(ServiceMontiorApplication.class, args);
	}

}
