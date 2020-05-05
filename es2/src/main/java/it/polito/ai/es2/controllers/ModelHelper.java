package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import org.springframework.hateoas.Link;

// TODO: aggiungere migliore self link
public class ModelHelper {
  public static CourseDTO enrich(CourseDTO courseDTO) {
    Link link = new Link("http://localhost:8080/API/courses/" + courseDTO.getName());
    courseDTO.add(link);
    return courseDTO;
  }
  
  public static StudentDTO enrich(StudentDTO studentDTO) {
    Link link = new Link("http://localhost:8080/API/students/" + studentDTO.getId());
    studentDTO.add(link);
    return studentDTO;
  }
}
