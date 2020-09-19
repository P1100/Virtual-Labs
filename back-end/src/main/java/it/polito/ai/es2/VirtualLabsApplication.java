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
        // To run, uncomment and also disable security (comment class security annotations in webConfig)
//          System.out.println("***************** Command Line Runner DB INITIALIZATION (check order of fields!) **********************");
//          courseService.addCourse(new CourseDTO("intapp", "Internet Applications", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("mobdev", "Mobile Development", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("sprogr", "System programming", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("softeng1", "Software Engineering I", 1, 500, true, null));
//          courseService.addCourse(new CourseDTO("softeng2", "Software Engineering II", 1, 500, true, null));
//
//          userService.addStudent(new StudentDTO(110100L, "Paolo", "Verdi", "s110100@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110200L, "Giuseppe", "Rossi", "s110200@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110300L, "Antonio", "Bianchi", "s110300@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110400L, "Angelo", "Verdi", "s110400@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110500L, "Domenico", "Gialli", "s110500@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110600L, "Bruno", "Ferri", "s110600@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110700L, "Paola", "Paleta", "s110700@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110800L, "Sergio", "Limari", "s110800@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(110900L, "Luciano", "Benterri", "s110900@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220100L, "Francesco", "Cavinni", "s220100@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220110L, "Maria", "Pasolani", "s220110@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220120L, "Valentina", "Gennari", "s220120@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220130L, "Francesca", "Tulini", "s220130@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220140L, "Elena", "Casellari", "s220140@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220150L, "Anna", "Rodieni", "s220150@studenti.polito.it", null, null, false, false, null, null));
//          userService.addStudent(new StudentDTO(220160L, "Last", "One", "s220160@studenti.polito.it", null, null, false, false, null, null));
//
//          courseService.enrollStudents(Collections.singletonList(110100L), "intapp");
//          courseService.enrollStudents(Arrays.asList(110500L, 220120L, 220100L, 220140L), "intapp");
      }
    };
  }
}
