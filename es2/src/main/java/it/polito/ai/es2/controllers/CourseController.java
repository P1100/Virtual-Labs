package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: versione finale passare a ultimo jdk java
@RestController
@RequestMapping("/API/courses")
public class CourseController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public List<CourseDTO> all() {
    List<CourseDTO> allCourses = teamService.getAllCourses();
    for (CourseDTO courseDTO : allCourses) {
      ModelHelper.enrich(courseDTO);
    }
//    Link link = linkTo(methodOn(CourseController.class)
//                           .all()).withSelfRel();
//    CollectionModel<CourseDTO> result = new CollectionModel<>(allCourses, link);
    return allCourses;
  }
  
  @GetMapping("/{name}")
  public CourseDTO getOne(@PathVariable String name) {
    Optional<CourseDTO> courseDTO = teamService.getCourse(name);
    if (!courseDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, name);
    return ModelHelper.enrich(courseDTO.get());
  }
  
  @GetMapping("/{name}/enrolled")
  public List<StudentDTO> enrolledStudents(@PathVariable String name) {
    List<StudentDTO> studentDTOlist = teamService.getEnrolledStudents(name);
    for (StudentDTO studentDTO : studentDTOlist) {
      ModelHelper.enrich(studentDTO);
    }
    return studentDTOlist;
  }
  
  //   {"name":"C33","min":1,"max":100,"enabled":true}
  // ---> Nella POST settare ContentType: application/json
  @PostMapping({"", "/"})
  public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
    if (!teamService.addCourse(courseDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseDTO.getName());
    } else
      return ModelHelper.enrich(courseDTO);
  }
  
  // ContentType:text/plain. Body:{"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  //  // TODO: cambiare stringa body id a Map<String,String>  ----> metti un Map<String,String> e verifica che ci sia la chiave “id”
  @PostMapping("/{courseName}/enrollOne")
  public void enrollOne(@PathVariable String courseName, @RequestBody Map<String, String> studentMap) {
    String studentId;
    if (studentMap.containsKey("id"))
      studentId = studentMap.get("id");
    else
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, courseName + " - studentMapReceived:" + studentMap);
    if (!teamService.addStudentToCourse(studentId, courseName)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseName + "-" + studentId);
    }
  }
  
  @PostMapping("/{courseName}/enrollMany")
  public List<Boolean> enrollStudents(@PathVariable String courseName, @RequestParam("file") MultipartFile file) {
    List<Boolean> booleanList = null;
    System.out.println(file.getContentType());
    if (!file.getContentType().equals("text/csv"))
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, courseName + " - CSV enrollStudents - Type:" + file.getContentType());
    if (file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseName + " - CSV enrollStudents");
    } else {
      // parse CSV file to create a list of `StudentViewModel` objects
      Reader reader;
      try {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        booleanList = teamService.addAndEroll(reader, courseName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return booleanList;
  }
}
