package it.polito.ai.es2;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.services.TeamService;
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
    CommandLineRunner runner(CourseRepository repo1c, StudentRepository repo2s, TeamService teamService, ModelMapper modelMapper) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                repo1c.findAll().stream().forEach(i -> System.out.println(i.toString()));
//                repo2s.findAll().stream().forEach(i -> System.out.println(i.toString()));

//                teamService.addCourse(new CourseDTO("C0", 1, 5, true));
/*

                teamService.addCourse(CourseDTO.builder().enabled(false).name("C1").min(1).max(10).build());
                teamService.addCourse(CourseDTO.builder().enabled(false).name("C2").min(1).max(20).build());
                teamService.addCourse(CourseDTO.builder().enabled(false).name("C3").min(1).max(30).build());
                teamService.addCourse(CourseDTO.builder().enabled(false).name("C4").min(1).max(40).build());
                teamService.addCourse(CourseDTO.builder().enabled(false).name("C5").min(1).max(50).build());
                teamService.addStudent(StudentDTO.builder().id("S1").firstName("fS1").name("nS1").build());
                teamService.addStudent(StudentDTO.builder().id("S2").firstName("fS2").name("nS2").build());
                teamService.addStudent(StudentDTO.builder().id("S3").firstName("fS3").name("nS3").build());
                teamService.addStudent(StudentDTO.builder().id("S4").firstName("fS4").name("nS4").build());
                System.out.println("---Duplicate:"+teamService.addStudent(StudentDTO.builder().id("S4").firstName("fS4").name("nS4").build()));
//                System.out.println("---Id null value:"+teamService.addStudent(StudentDTO.builder().id(null).firstName("fS4").name("fS4").build()));
//                StudentDTO sdt = StudentDTO.builder().id("S4").firstName(null).name(null).build();
                System.out.println("---Others values null:"+teamService.addStudent(StudentDTO.builder().id("S4").firstName(null).name(null).build()));
                System.out.println("C1 is present():"+teamService.getCourse("C1").isPresent());
                teamService.getCourse("C1").ifPresent(x -> System.out.println(x));
                teamService.getAllCourses().forEach(System.out::println);
*/

                Course c = new Course(); c.setName("Course Test");c.setEnabled(true);c.setMin(1);c.setMax(100);
                System.out.println("@@@@@@@@@@@@@@@@ - " + modelMapper.map(c,CourseDTO.class));
//                CourseDTO c2 = new CourseDTO("Cdto1",1,100,true);
//                System.out.println("§§§§§§§§§§§§§§§§ - " + modelMapper.map(c2,Course.class));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Es2Application.class, args);
    }
}
