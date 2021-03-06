package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.services.interfaces.CourseService;
import it.polito.ai.es2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
  @Autowired
  private CourseService courseService;

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

  @GetMapping("/{student_id}/courses")
  public CollectionModel<CourseDTO> getEnrolledCourses(@PathVariable Long student_id) {
    List<CourseDTO> courses = courseService.getEnrolledCourses(student_id);
    for (CourseDTO courseDTO : courses) {
      modelHelper.enrich(courseDTO);
    }
    CollectionModel<CourseDTO> courseDTOS = CollectionModel.of(courses,
        linkTo(methodOn(APIStudents_RestController.class).getEnrolledCourses(student_id)).withSelfRel());
    return courseDTOS;
  }
}
