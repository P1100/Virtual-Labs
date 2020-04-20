package it.polito.ai.es2;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.services.TeamService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Log
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
                /* SHOULD NOT WORK WITH LAZY LOADING */
//                repo1c.findAll().stream().forEach(i -> System.out.println(i.toString()));
//                repo2s.findAll().stream().forEach(i -> System.out.println(i.toString()));

                System.out.println("------------------------ BEGIN TEST MAPPING COMMAND LINE RUNNER -----------------------");
                Course crst = new Course();
                crst.setName("Course Test");
                crst.setEnabled(true);
                crst.setMin(1);
                crst.setMax(100);
                System.out.println("@@@@@@@@@@@@@@@@ - " + modelMapper.map(crst, CourseDTO.class));
                CourseDTO c0 = new CourseDTO("Cdto0", 1, 100, true);
                System.out.println("§§§§§§§§§§§§§§§§ - " + modelMapper.map(c0, Course.class));

                System.out.println("------------------------ BEGIN TEST SERVICE COMMAND LINE RUNNER -----------------------");
                teamService.addCourse(new CourseDTO("C0", 1, 2, false));
                CourseDTO c1 = new CourseDTO("Cdto1", 1, 100, true);
                teamService.addCourse(c1);
                CourseDTO c2 = new CourseDTO("Cdto2", 1, 100, true);
                teamService.addCourse(c2);
                CourseDTO c3 = new CourseDTO("Cdto3", 1, 99, false);
                teamService.addCourse(c3);

                StudentDTO s1dto = new StudentDTO("s1dto", "s1dto-name", "s1dto-FirstName");
                teamService.addStudent(s1dto);
                StudentDTO s2dto = new StudentDTO("s2dto", "s2dto-name", "s2dto-FirstName");
                teamService.addStudent(s2dto);
                StudentDTO s3dto = new StudentDTO("s3dto", "s3dto-name", "s3dto-FirstName");
                teamService.addStudent(s3dto);

                System.out.println("#C1 is present():" + teamService.getCourse("C1").isPresent());
                teamService.getCourse("C1").ifPresent(x -> System.out.println(x));
                System.out.println("#C99 is present():" + teamService.getCourse("C99").isPresent());
                teamService.getCourse("C99").ifPresent(x -> System.out.println(x));

                System.out.println("#s1dto is present():" + teamService.getStudent("s1dto").isPresent());
                teamService.getStudent("s1dto").ifPresent(x -> System.out.println(x));
                System.out.println("#S999 is present():" + teamService.getStudent("S999").isPresent());
                teamService.getStudent("S999").ifPresent(x -> System.out.println(x));

                teamService.getAllCourses().forEach(System.out::println);
                teamService.getAllStudents().forEach(System.out::println);

                teamService.enableCourse("C3");
                teamService.disableCourse("C0");

                System.out.println("-------------------------  AddStudentToCourse ------------------------------------");
                teamService.addStudentToCourse("S1", "C0");
                System.out.println("-------------------------  2nd AddStudentToCourse ------------------------------------");
                teamService.addStudentToCourse("S2", "C0");
                teamService.addStudentToCourse("S3", "C1");
                System.out.println("-------------------------  GetEnrolledStudents ------------------------------------");
                System.out.println(teamService.getEnrolledStudents("C0"));



               /*
                teamService.addStudent(StudentDTO.builder().id("S1").firstName("fS1").name("nS1").build());
                System.out.println("---Duplicate:"+teamService.addStudent(StudentDTO.builder().id("S4").firstName("fS4").name("nS4").build()));
//                System.out.println("---Id null value:"+teamService.addStudent(StudentDTO.builder().id(null).firstName("fS4").name("fS4").build()));
//                StudentDTO sdt = StudentDTO.builder().id("S4").firstName(null).name(null).build();
                System.out.println("---Others values null:"+teamService.addStudent(StudentDTO.builder().id("S4").firstName(null).name(null).build()));
*/
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Es2Application.class, args);
    }
}
