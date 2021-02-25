package ch.diedreifragezeichen.exama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import ch.diedreifragezeichen.exama.subject.SubjectRepository;
import ch.diedreifragezeichen.exama.subject.Subject;
import ch.diedreifragezeichen.exama.subject.SubjectService;

@SpringBootApplication
@ComponentScan
public class ExamaApplication {

	public static void main(String[] args) {

		SpringApplication.run(ExamaApplication.class, args);

	}

}
