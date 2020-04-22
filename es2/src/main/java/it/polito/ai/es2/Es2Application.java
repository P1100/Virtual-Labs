package it.polito.ai.es2;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.services.TeamService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Log
public class Es2Application {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner runner(CourseRepository cr, StudentRepository sr, TeamRepository tr, TeamService teamService, ModelMapper modelMapper) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                /* DONT WORK WITH LAZY LOADING */
//                cr.findAll().stream().forEach(i -> System.out.println(i.toString()));
//                sr.findAll().stream().forEach(i -> System.out.println(i.toString()));

                sr.deleteAll();
                cr.deleteAll();
                tr.deleteAll();
                System.out.println("############################## BEGIN TEST SERVICE COMMAND LINE RUNNER ####################################");
                teamService.addCourse(new CourseDTO("c_enroll_all", 1, 2, false));
                CourseDTO c0 = new CourseDTO("C0", 1, 100, true);
                teamService.addCourse(c0);
                CourseDTO c1 = new CourseDTO("C1", 1, 100, true);
                teamService.addCourse(c1);
                CourseDTO c2 = new CourseDTO("C2", 1, 100, true);
                teamService.addCourse(c2);
                CourseDTO c3 = new CourseDTO("C3", 1, 99, true);
                teamService.addCourse(c3);
                CourseDTO c4 = new CourseDTO("C4", 1, 99, true);
                teamService.addCourse(c4);
                CourseDTO c5 = new CourseDTO("C5", 1, 99, true);
                teamService.addCourse(c5);
                CourseDTO c6 = new CourseDTO("C6", 1, 99, true);
                teamService.addCourse(c6);
                CourseDTO c7 = new CourseDTO("C7", 1, 99, true);
                teamService.addCourse(c7);
                CourseDTO c8 = new CourseDTO("C8", 1, 99, false);
                teamService.addCourse(c8);
                StudentDTO s0 = new StudentDTO("S0", "S0-name", "S0-FirstName");
                teamService.addStudent(s0);
                StudentDTO s1 = new StudentDTO("S1", "S1-name", "S1-FirstName");
                teamService.addStudent(s1);
                StudentDTO s2 = new StudentDTO("S2", "S2-name", "S2-FirstName");
                teamService.addStudent(s2);
                StudentDTO s3 = new StudentDTO("S3", "S3-name", "S3-FirstName");
                teamService.addStudent(s3);
                StudentDTO s4 = new StudentDTO("S4", "S4-name", "S4-FirstName");
                teamService.addStudent(s4);
                StudentDTO s5 = new StudentDTO("S5", "S5-name", "S5-FirstName");
                teamService.addStudent(s5);
                StudentDTO s6 = new StudentDTO("S6", "S5-name", "S5-FirstName");
                teamService.addStudent(s6);
                StudentDTO s7 = new StudentDTO("S7", "S5-name", "S5-FirstName");
                teamService.addStudent(s7);
                StudentDTO s8 = new StudentDTO("S8", "S5-name", "S5-FirstName");
                teamService.addStudent(s8);
                StudentDTO s9 = new StudentDTO("S9", "S5-name", "S5-FirstName");
                teamService.addStudent(s9);

                System.out.println("-------------------------  getCourse() ------------------------------------");
                System.out.print("#C1 is present():" + teamService.getCourse("C1").isPresent() + "-");
                teamService.getCourse("C1").ifPresent(x -> System.out.println(x));
                System.out.println("#C99 is present():" + teamService.getCourse("C99").isPresent());
                teamService.getCourse("C99").ifPresent(x -> System.out.println(x));

                System.out.println("-------------------------  getStudent() ------------------------------------");
                System.out.print("S5 is present():" + teamService.getStudent("S5").isPresent() + "-");
                teamService.getStudent("S5").ifPresent(x -> System.out.println(x));
                System.out.println("#S999 is present():" + teamService.getStudent("S999").isPresent());
                teamService.getStudent("S999").ifPresent(x -> System.out.println(x));

                System.out.println("-------------------------  getAllCourses() ------------------------------------");
                teamService.getAllCourses().forEach(System.out::println);
                System.out.println("-------------------------  getAllStudents() ------------------------------------");
                teamService.getAllStudents().forEach(System.out::println);

                teamService.enableCourse("c_enroll_all");
                teamService.disableCourse("C7");

                System.out.println("-------------------------  1st AddStudentToCourse ------------------------------------");
                System.out.println("Result_1: " + teamService.addStudentToCourse("S1", "C0"));
                System.out.println("-------------------------  2nd AddStudentToCourse ------------------------------------");
                System.out.println("Result_2: " + teamService.addStudentToCourse("S2", "C0"));
                System.out.println("-------------------------  3rd AddStudentToCourse ------------------------------------");
                System.out.println("Result_3: " + teamService.addStudentToCourse("S2", "C1"));
                //teamService.addStudentToCourse("S34124124", "C13123123"); --> exception throwed, as intended!
                System.out.println("-------------------------  GetEnrolledStudents ------------------------------------");
                System.out.println(teamService.getEnrolledStudents("C0"));
                System.out.println("-------------------------  AddAll ------------------------------------");
                List<StudentDTO> lstudto = new ArrayList<>();
                lstudto = Arrays.asList(new StudentDTO("Student_0", "s0n", "s0fn"),
                        new StudentDTO("Student_1", "s1n", "s1fn"),
                        new StudentDTO("Student_2", "s2n", "s2fn"),
                        new StudentDTO("Student_3", "s3n", "s3fn"));
                System.out.println(teamService.addAll(lstudto));
                System.out.println("------------------------- GetCourses(student_id) ------------------------");
                System.out.println(teamService.getCourses("S3"));
                System.out.println("-------------------------  EnrollAll ------------------------------------");
                System.out.println(teamService.enrollAll(Arrays.asList("S1", "S4", "S5"), "C_enroll_all"));

                System.out.println("------  Team-Course Insertion Test --");
                teamService.addStudentToCourse("S0", "C0");
                teamService.addStudentToCourse("S1", "C0");
                teamService.addStudentToCourse("S2", "C0");
                teamService.addStudentToCourse("S3", "C0");
                teamService.addStudentToCourse("S5", "C3");
                teamService.addStudentToCourse("S6", "C3");
                teamService.addStudentToCourse("S9", "C3");
                teamService.proposeTeam("C0", "Team1", Arrays.asList("S0", "S1", "S2"));
                teamService.proposeTeam("C3", "Team1", Arrays.asList("S5", "S6"));

                System.out.println("------------------- getTeamForCourse ---------------");
                System.out.println(teamService.getTeamForCourse("C0"));

                System.out.println("------------------- getTeamsForStudent ---------------");
                System.out.println(teamService.getTeamsForStudent("S1"));

                System.out.println("------------------- getStudentsInTeams ---------------");
                System.out.println(teamService.getStudentsInTeams("C0"));

                System.out.println("------------------- getAvailableStudents ---------------");
                System.out.println(teamService.getAvailableStudents("C0"));
                System.out.println("############### END COMMAND LINE RUNNER #################");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Es2Application.class, args);
    }
}
