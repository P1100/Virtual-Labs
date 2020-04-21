package it.polito.ai.es2;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Team;
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
                /* SHOULD NOT WORK WITH LAZY LOADING */
//                cr.findAll().stream().forEach(i -> System.out.println(i.toString()));
//                sr.findAll().stream().forEach(i -> System.out.println(i.toString()));

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
                teamService.addCourse(new CourseDTO("c_enroll_all", 1, 2, false));
                CourseDTO Cdto1 = new CourseDTO("Cdto1", 1, 100, true);
                teamService.addCourse(Cdto1);
                CourseDTO Cdto2 = new CourseDTO("Cdto2", 1, 100, true);
                teamService.addCourse(Cdto2);
                CourseDTO Cdto3 = new CourseDTO("Cdto3", 1, 99, false);
                teamService.addCourse(Cdto3);
                CourseDTO C1 = new CourseDTO("C1", 1, 100, true);
                teamService.addCourse(C1);
                CourseDTO C2 = new CourseDTO("C2", 1, 100, true);
                teamService.addCourse(C2);
                CourseDTO C3 = new CourseDTO("C3", 1, 99, false);
                teamService.addCourse(C3);

                StudentDTO stu0 = new StudentDTO("stu0", "stu0-name", "stu0-FirstName");
                teamService.addStudent(stu0);
                StudentDTO s1dto = new StudentDTO("s1dto", "s1dto-name", "s1dto-FirstName");
                teamService.addStudent(s1dto);
                StudentDTO s2dto = new StudentDTO("s2dto", "s2dto-name", "s2dto-FirstName");
                teamService.addStudent(s2dto);
                StudentDTO s3dto = new StudentDTO("s3dto", "s3dto-name", "s3dto-FirstName");
                teamService.addStudent(s3dto);
                StudentDTO S1 = new StudentDTO("S1", "S1-name", "S1-FirstName");
                teamService.addStudent(S1);
                StudentDTO S2 = new StudentDTO("S2", "S2-name", "S2-FirstName");
                teamService.addStudent(S2);
                StudentDTO S3 = new StudentDTO("S3", "S3-name", "S3-FirstName");
                teamService.addStudent(S3);
                StudentDTO S4 = new StudentDTO("S4", "S4-name", "S4-FirstName");
                teamService.addStudent(S4);

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
                //teamService.addStudentToCourse("S34124124", "C13123123"); --> exception throwed, as intented!
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
                System.out.println(teamService.enrollAll(Arrays.asList("S1", "S4", "stu0"), "C_enroll_all"));

                System.out.println("-------------------------  Team Insertion Test  -------------------------");
  /*              Team t0 = new Team("T0",1);
                tr.save(t0);
                log.info(t0.toString());
                Team t1 = new Team("T1",1);
                tr.save(t1);
//                tr.flush();
                Course ct0 = new Course("CT0", 1, 100, true);
                t1.setCourse(ct0);
                ct0.setTeams(Arrays.asList(t1));
                tr.saveAndFlush(t1);
//                cr.saveAndFlush(ct0);
//                ct0.addTeam(t1);
//                tr.flush();
//                cr.flush();
                log.info(ct0.toString());
*/

                /* VA?
                Team t1 = new Team("T1",1);
//                tr.save(t1);
                Course ct0 = new Course("CT0", 1, 100, true);
                cr.save(ct0);
                ct0.addTeam(t1);
                cr.save(ct0);
                tr.save(t1);
                tr.flush();
                cr.flush();
*/
/*
      // NON VA?
                Team t2 = new Team("TT1",1);
//                tr.save(t2);
                log.info(t2.toString());
                Course ct1 = new Course("CTT1", 1, 100, true);
//                cr.saveAndFlush(ct1);
                ct1.addTeam(t2);
                tr.saveAndFlush(t2);
                cr.saveAndFlush(ct1);
                */

                System.out.println("-------------------------  Team Insertion Test  -------------------------");
                Team t1 = new Team("T1", 1);
                tr.save(t1);
                tr.flush();
                Course ct0 = new Course("CT0", 1, 100, true);
                cr.save(ct0);
//                ct0.addTeam(t1);
                t1.setCourse(ct0);
                ct0.setTeams(Arrays.asList(t1));
                cr.saveAndFlush(ct0);
/*




                System.out.println("############### END COMMAND LINE RUNNER #################");
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
