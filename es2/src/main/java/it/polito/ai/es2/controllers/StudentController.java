package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/API/students")
public class StudentController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public List<StudentDTO> all() {
    List<StudentDTO> allCourses = teamService.getAllStudents();
    for (StudentDTO studentDTO : allCourses) {
      ModelHelper.enrich(studentDTO);
    }
    return allCourses;
  }
  
  @GetMapping("/{id}")
  public StudentDTO getOne(@PathVariable String id) {
    // TODO: controllo optional later
    Optional<StudentDTO> studentDTO = teamService.getStudent(id);
    if (!studentDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, id);
    return ModelHelper.enrich(studentDTO.get());
  }
  
  //  {"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  @PostMapping({"", "/"})
  public StudentDTO addStudent(@RequestBody StudentDTO studentDTO) {
    if (!teamService.addStudent(studentDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, studentDTO.getName());
    } else
      return ModelHelper.enrich(studentDTO);
  }
}
