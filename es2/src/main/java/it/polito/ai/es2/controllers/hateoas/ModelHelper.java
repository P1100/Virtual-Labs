package it.polito.ai.es2.controllers.hateoas;

import it.polito.ai.es2.controllers.APICourses_RestController;
import it.polito.ai.es2.controllers.APIStudent_RestController;
import it.polito.ai.es2.controllers.APITeams_RestController;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ModelHelper {
  public static CourseDTO enrich(CourseDTO courseDTO) {
    courseDTO.add(Link.of("http://localhost:8080/API/courses", "courses"));
    courseDTO.add(new Link("http://localhost:8080/API/courses/" + courseDTO.getId()).withSelfRel());
    courseDTO.add(new Link("http://localhost:8080/API/courses/" + courseDTO.getId() + "/enable").withRel("enable (POST)"));
    courseDTO.add(new Link("http://localhost:8080/API/courses/" + courseDTO.getId() + "/disable").withRel("disable (POST)"));
  
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
                             .getEnrolledStudents(courseDTO.getId())).withRel("enrolled"));
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
                             .getStudentsInTeams(courseDTO.getId())).withRel("students_in_teams"));
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
                             .getAvailableStudents(courseDTO.getId())).withRel("students_available"));
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
                             .getTeamsForCourse(courseDTO.getId())).withRel("teams"));
    return courseDTO;
  }
  
  public static StudentDTO enrich(StudentDTO studentDTO) {
    studentDTO.add(new Link("http://localhost:8080/API/students").withRel("students"));
    studentDTO.add(new Link("http://localhost:8080/API/students/" + studentDTO.getId()).withSelfRel());
    studentDTO.add(linkTo(methodOn(APIStudent_RestController.class)
                              .getCourses(studentDTO.getId())).withRel("courses"));
    studentDTO.add(linkTo(methodOn(APIStudent_RestController.class)
                              .getTeamsForStudent(studentDTO.getId())).withRel("teams"));
    return studentDTO;
  }
  
  public static TeamDTO enrich(TeamDTO teamDTO) {
    teamDTO.add(new Link("http://localhost:8080/API/teams").withRel("teams"));
    teamDTO.add(new Link("http://localhost:8080/API/teams/" + teamDTO.getId()).withSelfRel());
    teamDTO.add(linkTo(methodOn(APITeams_RestController.class)
                           .getMembers(teamDTO.getId())).withRel("members"));
    return teamDTO;
  }
}
