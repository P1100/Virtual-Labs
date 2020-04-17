package it.polito.ai.es2;

import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Es2Application {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner runner(CourseRepository repo1c, StudentRepository repo2s) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                repo1c.findAll().stream().forEach(i -> System.out.println(i.toString()));
                repo2s.findAll().stream().forEach(i -> System.out.println(i.toString()));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Es2Application.class, args);
    }
}
