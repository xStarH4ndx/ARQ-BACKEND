package com.arquitectura.arquitectura;

import org.springframework.boot.SpringApplication;

public class TestArquitecturaApplication {

	public static void main(String[] args) {
		SpringApplication.from(ArquitecturaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
