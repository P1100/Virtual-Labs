package it.polito.ai.es2.controllers.hateoas;

import it.polito.ai.es2.controllers.APICourses_RestController;
import it.polito.ai.es2.controllers.APIStudents_RestController;
import it.polito.ai.es2.controllers.APITeams_RestController;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Used for: HATEOAS, retrieving url settings.
 */
@Component
public class ModelHelper {
  @Value("${server.port}")
  private String port;
  @Value("${server.address}")
  private String address;
  @Value("${myprop.prefixurl}")
  private String httpPrefix; // http or https
  public String baseUrl = "";

  @PostConstruct
  public void init() {
    baseUrl = httpPrefix + "://" + address + ":" + port;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public CourseDTO enrich(CourseDTO courseDTO) {
    if (courseDTO == null)
      return new CourseDTO();
    courseDTO.add(Link.of(baseUrl + "/api/courses", "courses"));
    courseDTO.add(Link.of(baseUrl + "/api/courses/" + courseDTO.getId(), IanaLinkRelations.SELF));
    courseDTO.add(Link.of(baseUrl + "/api/courses/" + courseDTO.getId() + "/enable").withRel("enable (POST)"));
    courseDTO.add(Link.of(baseUrl + "/api/courses/" + courseDTO.getId() + "/disable").withRel("disable (POST)"));

    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
        .getEnrolledStudents(courseDTO.getId())).withRel("enrolled"));
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
        .getEnrolledWithTeam(courseDTO.getId())).withRel("students-in-teams"));
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
        .getEnrolledWithoutTeam(courseDTO.getId())).withRel("students-available"));
    courseDTO.add(linkTo(methodOn(APICourses_RestController.class)
        .getTeamsForCourse(courseDTO.getId())).withRel("teams"));
    return courseDTO;
  }

  public StudentDTO enrich(StudentDTO studentDTO) {
    if (studentDTO == null)
      return new StudentDTO();
    studentDTO.add(Link.of(baseUrl + "/api/students").withRel("students"));
    studentDTO.add(Link.of(baseUrl + "/api/students/" + studentDTO.getId()).withSelfRel());
    studentDTO.add(linkTo(methodOn(APIStudents_RestController.class)
        .getEnrolledCourses(studentDTO.getId())).withRel("courses"));
    studentDTO.add(linkTo(methodOn(APIStudents_RestController.class)
        .getTeamsForStudent(studentDTO.getId())).withRel("teams"));
    return studentDTO;
  }

  public TeamDTO enrich(TeamDTO teamDTO) {
    if (teamDTO == null)
      return new TeamDTO();
    teamDTO.add(Link.of(baseUrl + "/api/teams").withRel("teams"));
    teamDTO.add(Link.of(baseUrl + "/api/teams/" + teamDTO.getId()).withSelfRel());
    teamDTO.add(linkTo(methodOn(APITeams_RestController.class)
                           .getMembers(teamDTO.getId())).withRel("members"));
    return teamDTO;
  }
}
