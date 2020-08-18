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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: test empty db, empty courses/tables, runtime errors, null values, invalid inputs and constrains, conversion and arithmetic errors, sub methods errors
@RestController
@RequestMapping("/API/courses")
public class APICourses_RestController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public CollectionModel<CourseDTO> getAllCourses() {
    List<CourseDTO> courses = teamService.getAllCourses().stream().map(ModelHelper::enrich).collect(Collectors.toList());
    CollectionModel<CourseDTO> coursesHAL = CollectionModel.of(courses,
        linkTo(methodOn(APICourses_RestController.class).getAllCourses()).withSelfRel());
    return coursesHAL;
  }
  
  @GetMapping("/{courseId}")
  public CourseDTO getCourse(@PathVariable String courseId) {
    Optional<CourseDTO> courseDTO = teamService.getCourse(courseId);
    if (courseDTO.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found - " + courseId);
    return ModelHelper.enrich(courseDTO.get());
  }
  
  // TODO: remove GET in production
  @RequestMapping(value = "/{course}/enable", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
  public void enableCourse(@PathVariable String course) {
    teamService.enableCourse(course);
  }
  
  // TODO: remove GET in production
  @RequestMapping(value = "/{course}/disable", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
  public void disableCourse(@PathVariable String course) {
    teamService.disableCourse(course);
  }
  
  //   {"name":"C33","min":1,"max":100,"enabled":true,"professor":"malnati"} - ContentType: application/json
  @PostMapping({"", "/"})
  public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
    if (!teamService.addCourse(courseDTO)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseDTO.getId());
    } else
      return ModelHelper.enrich(courseDTO);
  }
  
  @GetMapping("/{courseId}/enrolled")
  public CollectionModel<StudentDTO> getEnrolledStudents(@PathVariable String courseId) {
    List<StudentDTO> students = teamService.getEnrolledStudents(courseId);
    for (StudentDTO student : students) {
      ModelHelper.enrich(student);
    }
    CollectionModel<StudentDTO> studentsHAL = CollectionModel.of(students,
        linkTo(methodOn(APICourses_RestController.class).getEnrolledStudents(courseId)).withSelfRel());
    return studentsHAL;
  }
  
  @GetMapping("/{courseId}/students-in-teams")
  public CollectionModel<StudentDTO> getStudentsInTeams(@PathVariable String courseId) {
    List<StudentDTO> students = teamService.getStudentsInTeams(courseId);
    for (StudentDTO student : students) {
      ModelHelper.enrich(student);
    }
    CollectionModel<StudentDTO> studentsHAL = CollectionModel.of(students,
        linkTo(methodOn(APICourses_RestController.class).getStudentsInTeams(courseId)).withSelfRel());
    return studentsHAL;
  }
  
  @GetMapping("/{courseId}/students-available")
  public CollectionModel<StudentDTO> getAvailableStudents(@PathVariable String courseId) {
    List<StudentDTO> students = teamService.getAvailableStudents(courseId);
    for (StudentDTO student : students) {
      ModelHelper.enrich(student);
    }
    CollectionModel<StudentDTO> studentsHAL = CollectionModel.of(students,
        linkTo(methodOn(APICourses_RestController.class).getAvailableStudents(courseId)).withSelfRel());
    return studentsHAL;
  }
  
  @GetMapping("/{courseId}/teams")
  public CollectionModel<TeamDTO> getTeamsForCourse(@PathVariable String courseId) {
    List<TeamDTO> teams = teamService.getTeamsForCourse(courseId);
    for (TeamDTO team : teams) {
      ModelHelper.enrich(team);
    }
    CollectionModel<TeamDTO> teamsHAL = CollectionModel.of(teams,
        linkTo(methodOn(APICourses_RestController.class).getTeamsForCourse(courseId)).withSelfRel());
    return teamsHAL;
  }
  
  @PutMapping("/{courseId}/disenroll/{studentId}")
  public void disenrollStudent(@PathVariable Long studentId, @PathVariable String courseId) {
    teamService.disenrollStudent(studentId, courseId);
  }
  
  // ContentType:json. Body:{"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
  @RequestMapping(value = "/{courseId}/enroll", method = {RequestMethod.PUT, RequestMethod.POST})
  public void enrollStudent(@PathVariable String courseId, @RequestBody Map<String, Long> studentMap) {
    Long studentId;
    if (studentMap.containsKey("id"))
      studentId = studentMap.get("id");
    else
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, courseId + " - studentMapReceived:" + studentMap);
    if (!teamService.enrollStudent(studentId, courseId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseId + "-" + studentId);
    }
  }
  
  //["S33","S44"]
  @PostMapping("/{courseId}/enroll-all")
  public List<Boolean> enrollStudents(@RequestBody List<Long> studentIds, @PathVariable String courseId) {
    return teamService.enrollStudents(studentIds, courseId);
  }
  
  @PostMapping("/{courseId}/enroll-csv")
  public List<Boolean> enrollStudentsCSV(@PathVariable String courseId, @RequestParam("file") MultipartFile file) {
    List<Boolean> booleanList = null;
    System.out.println(file.getContentType());
    if (!file.getContentType().equals("text/csv"))
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, courseId + " - CSV enrollStudentsCSV - Type:" + file.getContentType());
    if (file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, courseId + " - CSV enrollStudentsCSV");
    } else {
      // parse CSV file to create a list of `StudentViewModel` objects
      Reader reader;
      try {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        booleanList = teamService.addAndEroll(reader, courseId);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return booleanList;
  }
}
