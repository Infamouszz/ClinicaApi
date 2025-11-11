package com.projeto.maedopedro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.projeto.maedopedro.Repository")
@EntityScan("com.projeto.maedopedro.Model")
public class MaedopedroApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaedopedroApplication.class, args);
	}

}
