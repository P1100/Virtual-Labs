package it.polito.ai.es2;

import it.polito.ai.es2.services.interfaces.CourseService;
import it.polito.ai.es2.services.interfaces.TeamService;
import it.polito.ai.es2.services.interfaces.UserService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Invalid Inputs: empty db, empty entities, course enabled, same name team, student not enrolled, 1 team per course x student,
 * Other errors: runtime errors, null values, conversion and arithmetic errors, sub methods errors, SYNCH ENTITIES MISSING
 * Multi data input error (and other controller input mismatch)
 */
@SpringBootApplication
@Log
public class VirtualLabsApplication {
  public static void main(String[] args) {
    SpringApplication.run(VirtualLabsApplication.class, args);
  }

  @Autowired
  private CourseService courseService;
  @Autowired
  private UserService userService;
  @Autowired
  private TeamService teamService;

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  CommandLineRunner runner() {
    return new CommandLineRunner() {
      @Override
      public void run(String... args) {
//        // TODO: remove in prod, add external sql script. Note: MUST COMMENT SECURITY 'HASROLE' PREAUTHORIZE on methods, before executing
        // To run, uncomment and also disable security (comment class security annotations in webConfig)
//          System.out.println("***************** Command Line Runner DB INITIALIZATION (check order of fields!) **********************");
//          courseService.addCourse(new CourseDTO("c1", "Internet Applications", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("c2", "Mobile Development", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("c3", "System and device programming", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("c4", "Software Engineering I", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("c5", "Software Engineering II", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("c6", "Test: Disabled Course", 1, 500, false, null));
//          courseService.addCourse(new CourseDTO("c7", "Test: min3 max5", 3, 5, true, null));
//
//          userService.addStudent(new StudentDTO(1L, "Paolo", "Verdi", "s1@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(2L, "Giuseppe", "Rossi", "s2@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(3L, "Antonio", "Bianchi", "s3@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(4L, "Angelo", "Verdi", "s4@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(5L, "Domenico", "Gialli", "s5@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(6L, "Bruno", "Ferri", "s6@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(7L, "Paola", "Paleta", "s7@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(8L, "Sergio", "Limari", "s8@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(9L, "Luciano", "Benterri", "s9@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(10L, "Francesco", "Cavinni", "s10@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(11L, "Maria", "Pasolani", "s11@studenti.polito.it", null, null, false, false, null, null));
//
//          userService.addStudent(new StudentDTO(12L, "Valentina", "Gennari", "s12@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(13L, "Francesca", "Tulini", "s13@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(14L, "Elena", "Casellari", "s14@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(15L, "Anna", "Rodieni", "s15@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(100L, "Last", "One", "s16@studenti.polito.it", null, null, false, false, null, null));
//
//          courseService.enrollStudents(Collections.singletonList(2L), "c5");
//          courseService.enrollStudents(Arrays.asList(1L, 2L, 3L, 4L), "c1");
      }
    };
  }
}
