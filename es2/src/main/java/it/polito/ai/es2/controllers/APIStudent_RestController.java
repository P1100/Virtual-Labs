package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/API/students")
public class APIStudent_RestController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public CollectionModel<StudentDTO> getAllStudents() {
    List<StudentDTO> allStudents = teamService.getAllStudents();
    for (StudentDTO studentDTO : allStudents) {
      ModelHelper.enrich(studentDTO);
    }
    Link link = linkTo(methodOn(APIStudent_RestController.class)
                           .getAllStudents()).withSelfRel();
    CollectionModel<StudentDTO> result = new CollectionModel<>(allStudents, link);
    return result;
  }
  
  @GetMapping("/{student_id}")
  public StudentDTO getStudent(@PathVariable Long student_id) {
    Optional<StudentDTO> studentDTO = teamService.getStudent(student_id);
    if (!studentDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, student_id.toString());
    return ModelHelper.enrich(studentDTO.get());
  }
  
  @GetMapping("/{student_id}/courses")
  public CollectionModel<CourseDTO> getCourses(@PathVariable Long student_id) {
    List<CourseDTO> courses = teamService.getCourses(student_id);
    for (CourseDTO courseDTO : courses) {
      ModelHelper.enrich(courseDTO);
    }
    CollectionModel<CourseDTO> courseDTOS = CollectionModel.of(courses,
        linkTo(methodOn(APIStudent_RestController.class).getCourses(student_id)).withSelfRel());
    return courseDTOS;
  }
  
  @GetMapping("/{student_id}/teams")
  public CollectionModel<TeamDTO> getTeamsForStudent(@PathVariable Long student_id) {
    List<TeamDTO> teams = teamService.getTeamsForStudent(student_id);
    for (TeamDTO team : teams) {
      ModelHelper.enrich(team);
    }
    CollectionModel<TeamDTO> teamsHAL = CollectionModel.of(teams,
        linkTo(methodOn(APIStudent_RestController.class).getTeamsForStudent(student_id)).withSelfRel());
    return teamsHAL;
  }
  
  //  {"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  // ---> Nella POST settare ContentType: application/json
  @PostMapping({"", "/"})
  public StudentDTO addStudent(@RequestBody StudentDTO studentDTO) {
    if (!teamService.addStudent(studentDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, studentDTO.getLastName());
    } else
      return ModelHelper.enrich(studentDTO);
  }
  
  //  [{"id":"S44","name":"S33-name","firstName":"S33-FirstName"},{"id":"S55","name":"S33-name","firstName":"S33-FirstName"}]
  @PostMapping("/addall")
  public List<Boolean> addAll(@RequestBody List<StudentDTO> students) {
    return teamService.addAllStudents(students);
  }
}
