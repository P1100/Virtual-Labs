package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

@RestController
@RequestMapping("/API/courses")
public class APICourses_RestController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public CollectionModel<CourseDTO> getAllCourses() {
    List<CourseDTO> courses = teamService.getAllCourses();
//    teamService.getAllCourses().stream().map(ModelHelper::enrich).collect(Collectors.toList())
    for (CourseDTO course : courses) {
      ModelHelper.enrich(course);
    }
    CollectionModel<CourseDTO> coursesHAL = CollectionModel.of(courses,
        linkTo(methodOn(APICourses_RestController.class).getAllCourses()).withSelfRel());
    return coursesHAL;
  }
  
  @GetMapping("/{courseId}")
  public CourseDTO getCourse(@PathVariable String courseId) {
    Optional<CourseDTO> courseDTO = teamService.getCourse(courseId);
    if (!courseDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseId);
    return ModelHelper.enrich(courseDTO.get());
  }
  
  @RequestMapping(value = "/{course}/enable", method = {RequestMethod.GET, RequestMethod.POST})
  public void enableCourse(@PathVariable String course) {
    teamService.enableCourse(course);
  }
  
  @RequestMapping(value = "/{course}/disable", method = {RequestMethod.GET, RequestMethod.POST})
  public void disableCourse(@PathVariable String course) {
    teamService.disableCourse(course);
  }
  
  //   {"name":"C33","min":1,"max":100,"enabled":true,"professor":"malnati"}
  // ---> Nella POST settare ContentType: application/json
  @PostMapping({"", "/"})
  public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
    if (!teamService.addCourse(courseDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseDTO.getId());
    } else
      return ModelHelper.enrich(courseDTO);
  }
  
  @GetMapping("/{courseName}/enrolled")
  public CollectionModel<StudentDTO> getEnrolledStudents(@PathVariable String courseName) {
    List<StudentDTO> students = teamService.getEnrolledStudents(courseName);
    for (StudentDTO student : students) {
      ModelHelper.enrich(student);
    }
    CollectionModel<StudentDTO> studentsHAL = CollectionModel.of(students,
        linkTo(methodOn(APICourses_RestController.class).getEnrolledStudents(courseName)).withSelfRel());
    return studentsHAL;
  }
  
  @GetMapping("/{courseName}/students-in-teams")
  public CollectionModel<StudentDTO> getStudentsInTeams(@PathVariable String courseName) {
    List<StudentDTO> students = teamService.getStudentsInTeams(courseName);
    for (StudentDTO student : students) {
      ModelHelper.enrich(student);
    }
    CollectionModel<StudentDTO> studentsHAL = CollectionModel.of(students,
        linkTo(methodOn(APICourses_RestController.class).getStudentsInTeams(courseName)).withSelfRel());
    return studentsHAL;
  }
  
  @GetMapping("/{courseName}/students-available")
  public CollectionModel<StudentDTO> getAvailableStudents(@PathVariable String courseName) {
    List<StudentDTO> students = teamService.getAvailableStudents(courseName);
    for (StudentDTO student : students) {
      ModelHelper.enrich(student);
    }
    CollectionModel<StudentDTO> studentsHAL = CollectionModel.of(students,
        linkTo(methodOn(APICourses_RestController.class).getAvailableStudents(courseName)).withSelfRel());
    return studentsHAL;
  }
  
  @GetMapping("/{courseName}/teams")
  public CollectionModel<TeamDTO> getTeamsForCourse(@PathVariable String courseName) {
    List<TeamDTO> teams = teamService.getTeamsForCourse(courseName);
    for (TeamDTO team : teams) {
      ModelHelper.enrich(team);
    }
    CollectionModel<TeamDTO> teamsHAL = CollectionModel.of(teams,
        linkTo(methodOn(APICourses_RestController.class).getTeamsForCourse(courseName)).withSelfRel());
    return teamsHAL;
  }
  
  // ContentType:json. Body:{"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  @RequestMapping(value = "/{courseName}/enroll", method = {RequestMethod.PUT, RequestMethod.POST})
  public void enrollStudent(@PathVariable String courseName, @RequestBody Map<String, String> studentMap) {
    System.out.println(studentMap);
    String studentId;
    if (studentMap.containsKey("id"))
      studentId = studentMap.get("id");
    else
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, courseName + " - studentMapReceived:" + studentMap);
    if (!teamService.enrollStudent(studentId, courseName)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseName + "-" + studentId);
    }
  }
  
  //["S33","S44"]
  @PostMapping("/{courseName}/enroll-all")
  public List<Boolean> enrollStudents(@RequestBody List<String> studentIds, @PathVariable String courseName) {
    return teamService.enrollStudents(studentIds, courseName);
  }
  
  @PostMapping("/{courseName}/enroll-csv")
  public List<Boolean> enrollStudentsCSV(@PathVariable String courseName, @RequestParam("file") MultipartFile file) {
    List<Boolean> booleanList = null;
    System.out.println(file.getContentType());
    if (!file.getContentType().equals("text/csv"))
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, courseName + " - CSV enrollStudentsCSV - Type:" + file.getContentType());
    if (file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseName + " - CSV enrollStudentsCSV");
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
