package com.diacono.diacono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DiaconoApplication {
    // TODO: Controllers e UseCases Ministerios pendente de revisao OWASP v3
	public static void main(String[] args) {
		SpringApplication.run(DiaconoApplication.class, args);
	}
}
