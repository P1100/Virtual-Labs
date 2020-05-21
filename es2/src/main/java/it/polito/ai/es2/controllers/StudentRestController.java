package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.services.TeamService;
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
  
  //TODO: return CollectionModel instead of List + aggiungere operazioni possibili a json di ritorno
  @GetMapping({"", "/"})
  public CollectionModel<StudentDTO> getAllStudents() {
    List<StudentDTO> allCourses = teamService.getAllStudents();
    for (StudentDTO studentDTO : allCourses) {
      ModelHelper.enrich(studentDTO);
    }
    Link link = linkTo(methodOn(CourseRestController.class)
                           .getAllCourses()).withSelfRel();
    CollectionModel<StudentDTO> result = new CollectionModel<>(allCourses, link);
    return result;
  }
  
  @GetMapping("/{id}")
  public StudentDTO getOne(@PathVariable String id) {
    Optional<StudentDTO> studentDTO = teamService.getStudent(id);
    if (!studentDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, id);
    return ModelHelper.enrich(studentDTO.get());
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
}
