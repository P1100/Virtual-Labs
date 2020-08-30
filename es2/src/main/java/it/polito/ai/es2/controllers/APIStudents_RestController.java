package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/students")
public class APIStudents_RestController {
  @Autowired
  UserService userService;
  @Autowired
  private ModelHelper modelHelper;

  @GetMapping()
  public CollectionModel<StudentDTO> getAllStudents() {
    List<StudentDTO> allStudents = userService.getAllStudents();
    for (StudentDTO studentDTO : allStudents) {
      modelHelper.enrich(studentDTO);
    }
    Link link = linkTo(methodOn(APIStudents_RestController.class).getAllStudents()).withSelfRel();
    CollectionModel<StudentDTO> result = CollectionModel.of(allStudents, link);
    return result;
  }

  @GetMapping("/{student_id}")
  public StudentDTO getStudent(@PathVariable Long student_id) {
    Optional<StudentDTO> studentDTO = userService.getStudent(student_id);
    if (studentDTO.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, student_id.toString());
    return modelHelper.enrich(studentDTO.get());
  }

  //  {"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  // ---> Nella POST settare ContentType: application/json
  @PostMapping()
  public StudentDTO addStudent(@Valid @RequestBody StudentDTO studentDTO) {
    userService.addStudent(studentDTO);
    return modelHelper.enrich(studentDTO);
  }

  @GetMapping("/{student_id}/courses")
  public CollectionModel<CourseDTO> getCourses(@PathVariable Long student_id) {
    List<CourseDTO> courses = userService.getEnrolledCourses(student_id);
    for (CourseDTO courseDTO : courses) {
      modelHelper.enrich(courseDTO);
    }
    CollectionModel<CourseDTO> courseDTOS = CollectionModel.of(courses,
        linkTo(methodOn(APIStudents_RestController.class).getCourses(student_id)).withSelfRel());
    return courseDTOS;
  }

  @GetMapping("/{student_id}/teams")
  public CollectionModel<TeamDTO> getTeamsForStudent(@PathVariable Long student_id) {
    List<TeamDTO> teams = userService.getTeamsForStudent(student_id);
    for (TeamDTO team : teams) {
      modelHelper.enrich(team);
    }
    CollectionModel<TeamDTO> teamsHAL = CollectionModel.of(teams,
        linkTo(methodOn(APIStudents_RestController.class).getTeamsForStudent(student_id)).withSelfRel());
    return teamsHAL;
  }

}
