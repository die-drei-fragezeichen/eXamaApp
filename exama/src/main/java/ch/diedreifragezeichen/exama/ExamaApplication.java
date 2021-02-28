package ch.diedreifragezeichen.exama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ExamaApplication {

	public static void main(String[] args) {

		SpringApplication.run(ExamaApplication.class, args);

	}

}