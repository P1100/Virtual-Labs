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
import java.util.Optional;

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
    return allCourses;
  }
  
  @GetMapping("/{name}")
  public CourseDTO getOne(@PathVariable String name) {
    // TODO: controllo optional later
    Optional<CourseDTO> courseDTO = teamService.getCourse(name);
    if (!courseDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, name);
    return ModelHelper.enrich(courseDTO.get());
  }
  
  // TODO: aggiungere link a se stesso later
  @GetMapping("/{name}/enrolled")
  public List<StudentDTO> enrolledStudents(@PathVariable String name) {
    List<StudentDTO> studentDTOlist = teamService.getEnrolledStudents(name);
    for (StudentDTO studentDTO : studentDTOlist) {
      ModelHelper.enrich(studentDTO);
    }
    return studentDTOlist;
  }
  
  //   {"name":"C33","min":1,"max":100,"enabled":true}
  // ---> Nella POST settare COntentType json
  @PostMapping({"", "/"})
  public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
    if (!teamService.addCourse(courseDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseDTO.getName());
    } else
      return ModelHelper.enrich(courseDTO);
  }
  
  // {"id":"S0","name":"S0-name","firstName":"S0-FirstName"}
  @PostMapping("/{name}/enrollOne")
  public void enrollOne(@PathVariable String courseName, @RequestBody String studentId) {
    if (!teamService.addStudentToCourse(courseName, studentId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseName + "-" + studentId);
    }
  }
  
  // TODO: finire dopo
  @PostMapping("/{name}/enrollMany")
  public List<Boolean> enrollStudents(@PathVariable String courseName, @RequestParam("file") MultipartFile file) {
    List<Boolean> booleanList = null;
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
