package it.polito.ai.es2.controllers.hateoas;

import it.polito.ai.es2.controllers.CourseRestController;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ModelHelper {
  public static CourseDTO enrich(CourseDTO courseDTO) {
    Link link = new Link("http://localhost:8080/API/courses/" + courseDTO.getName()).withSelfRel();
//    Link selfLink = linkTo(methodOn(CourseController.class).all()).withSelfRel();    courseDTO.add(selfLink);
    courseDTO.add(link);
    Link enrolled = WebMvcLinkBuilder.linkTo(methodOn(CourseRestController.class)
                               .enrolledStudents(courseDTO.getName())).withRel("enrolled");
    courseDTO.add(enrolled);
    return courseDTO;
  }
  
  public static StudentDTO enrich(StudentDTO studentDTO) {
    Link link = new Link("http://localhost:8080/API/students/" + studentDTO.getId()).withSelfRel();
    studentDTO.add(link);
    return studentDTO;
  }
}
