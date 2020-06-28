package it.polito.ai.es2.controllers.hateoas;

import it.polito.ai.es2.controllers.CourseRestController;
import it.polito.ai.es2.controllers.StudentRestController;
import it.polito.ai.es2.controllers.TeamRestController;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ModelHelper {
  public static CourseDTO enrich(CourseDTO courseDTO) {
    courseDTO.add(new Link("http://localhost:8080/API/courses").withRel("courses"));
    courseDTO.add(new Link("http://localhost:8080/API/courses/" + courseDTO.getName()).withSelfRel());
    courseDTO.add(new Link("http://localhost:8080/API/courses/" + courseDTO.getName() + "/enable").withRel("enable (POST)"));
    courseDTO.add(new Link("http://localhost:8080/API/courses/" + courseDTO.getName() + "/disable").withRel("disable (POST)"));
  
    courseDTO.add(linkTo(methodOn(CourseRestController.class)
                             .getEnrolledStudents(courseDTO.getName())).withRel("enrolled"));
    courseDTO.add(linkTo(methodOn(CourseRestController.class)
                             .getStudentsInTeams(courseDTO.getName())).withRel("students_in_teams"));
    courseDTO.add(linkTo(methodOn(CourseRestController.class)
                             .getAvailableStudents(courseDTO.getName())).withRel("students_available"));
    courseDTO.add(linkTo(methodOn(CourseRestController.class)
                             .getTeamsForCourse(courseDTO.getName())).withRel("teams"));
    return courseDTO;
  }
  
  public static StudentDTO enrich(StudentDTO studentDTO) {
    studentDTO.add(new Link("http://localhost:8080/API/students").withRel("students"));
    studentDTO.add(new Link("http://localhost:8080/API/students/" + studentDTO.getId()).withSelfRel());
    studentDTO.add(linkTo(methodOn(StudentRestController.class)
                              .getCourses(studentDTO.getId())).withRel("courses"));
    studentDTO.add(linkTo(methodOn(StudentRestController.class)
                              .getTeamsForStudent(studentDTO.getId())).withRel("teams"));
    return studentDTO;
  }
  
  public static TeamDTO enrich(TeamDTO teamDTO) {
    teamDTO.add(new Link("http://localhost:8080/API/teams").withRel("teams"));
    teamDTO.add(new Link("http://localhost:8080/API/teams/" + teamDTO.getId()).withSelfRel());
    teamDTO.add(linkTo(methodOn(TeamRestController.class)
                           .getMembers(teamDTO.getId())).withRel("members"));
    return teamDTO;
  }
}
