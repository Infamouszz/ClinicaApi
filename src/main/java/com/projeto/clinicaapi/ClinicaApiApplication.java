package com.projeto.clinicaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.projeto.clinicaapi.Repository")
@EntityScan("com.projeto.clinicaapi.Model")
public class ClinicaApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClinicaApiApplication.class, args);
	}

}
