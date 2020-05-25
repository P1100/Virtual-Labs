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
public class StudentRestController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public CollectionModel<StudentDTO> getAllStudents() {
    List<StudentDTO> allStudents = teamService.getAllStudents();
    for (StudentDTO studentDTO : allStudents) {
      ModelHelper.enrich(studentDTO);
    }
    Link link = linkTo(methodOn(StudentRestController.class)
                           .getAllStudents()).withSelfRel();
    CollectionModel<StudentDTO> result = new CollectionModel<>(allStudents, link);
    return result;
  }
  
  @GetMapping("/{student_id}")
  public StudentDTO getStudent(@PathVariable String student_id) {
    Optional<StudentDTO> studentDTO = teamService.getStudent(student_id);
    if (!studentDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, student_id);
    return ModelHelper.enrich(studentDTO.get());
  }
  @GetMapping("/{student_id}/courses")
  public List<CourseDTO> getCourses(@PathVariable String student_id) {
    List<CourseDTO> courses = teamService.getCourses(student_id);
    for (CourseDTO courseDTO : courses) {
      ModelHelper.enrich(courseDTO);
    }
    return courses;
  }
  @GetMapping("/{student_id}/teams")
  public List<TeamDTO> getTeamsForStudent(@PathVariable String student_id) {
    List<TeamDTO> teams = teamService.getTeamsForStudent(student_id);
    for (TeamDTO teamDTO : teams) {
      ModelHelper.enrich(teamDTO);
    }
    return teams;
  }
  
  //  {"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  // ---> Nella POST settare ContentType: application/json
  @PostMapping({"", "/"})
  public StudentDTO addStudent(@RequestBody StudentDTO studentDTO) {
    if (!teamService.addStudent(studentDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, studentDTO.getName());
    } else
      return ModelHelper.enrich(studentDTO);
  }
  
  //  [{"id":"S44","name":"S33-name","firstName":"S33-FirstName"},{"id":"S55","name":"S33-name","firstName":"S33-FirstName"}]
  @PostMapping("/addall")
  public List<Boolean> addAll(@RequestBody List<StudentDTO> students) {
    return teamService.addAll(students);
  }
}
