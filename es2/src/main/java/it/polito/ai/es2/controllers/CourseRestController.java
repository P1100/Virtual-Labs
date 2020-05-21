package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: versione finale passare a ultimo jdk java
// TODO: aggiungere token jwt bearer nelle richieste (manualmente, per testing)
@RestController
@RequestMapping("/API/courses")
public class CourseRestController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public CollectionModel<CourseDTO> getAllCourses() {
    List<CourseDTO> allCourses = teamService.getAllCourses();
//    teamService.getAllCourses().stream().map(ModelHelper::enrich).collect(Collectors.toList())
    for (CourseDTO courseDTO : allCourses) {
      ModelHelper.enrich(courseDTO);
    }
    Link link = linkTo(methodOn(CourseRestController.class)
                           .getAllCourses()).withSelfRel();
    CollectionModel<CourseDTO> result = new CollectionModel<>(allCourses, link);
    return result;
  }
  
  @GetMapping("/{courseName}")
  public CourseDTO getCourse(@PathVariable String courseName) {
    Optional<CourseDTO> courseDTO = teamService.getCourse(courseName);
    if (!courseDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseName);
    return ModelHelper.enrich(courseDTO.get());
  }
  
  @GetMapping("/{courseName}/enrolled")
  public List<StudentDTO> getEnrolledStudents(@PathVariable String courseName) {
    List<StudentDTO> studentDTOlist = teamService.getEnrolledStudents(courseName);
    for (StudentDTO studentDTO : studentDTOlist) {
      ModelHelper.enrich(studentDTO);
    }
    return studentDTOlist;
  }
  
  @GetMapping("/{courseName}/students_inteams")
  public List<StudentDTO> getStudentsInTeams(@PathVariable String courseName) {
    List<StudentDTO> studentsInTeams = teamService.getStudentsInTeams(courseName);
    for (StudentDTO studentDTO : studentsInTeams) {
      ModelHelper.enrich(studentDTO);
    }
    return studentsInTeams;
  }
  
  @GetMapping("/{courseName}/students_available")
  public List<StudentDTO> getAvailableStudents(@PathVariable String courseName) {
    List<StudentDTO> studentsInTeams = teamService.getAvailableStudents(courseName);
    for (StudentDTO studentDTO : studentsInTeams) {
      ModelHelper.enrich(studentDTO);
    }
    return studentsInTeams;
  }
  
  @GetMapping("/{courseName}/teams")
  public List<TeamDTO> getTeamsForCourse(@PathVariable String courseName) {
    List<TeamDTO> teamsForCourse = teamService.getTeamsForCourse(courseName);
    for (TeamDTO teamDTO : teamsForCourse) {
      ModelHelper.enrich(teamDTO);
    }
    return teamsForCourse;
  }
  
  //   {"name":"C33","min":1,"max":100,"enabled":true}
  // ---> Nella POST settare ContentType: application/json
  @PostMapping({"", "/"})
  public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
    if (!teamService.addCourse(courseDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseDTO.getIdname());
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
  
  @PostMapping("/{course}/enable")
  public void enableCourse(@PathVariable String course) {
    teamService.enableCourse(course);
  }
  
  //TODO: methods missing from api, to add later (?!?)
  @PostMapping("/addall/{list}")
  public void addAll(List<StudentDTO> students) {
  }
}
