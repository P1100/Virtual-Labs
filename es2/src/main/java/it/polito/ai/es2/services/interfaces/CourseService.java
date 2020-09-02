package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import java.io.Reader;
import java.util.List;
import java.util.Optional;

public interface CourseService {
  List<CourseDTO> getAllCourses();

  Optional<CourseDTO> getCourse(String name);

  void addCourse(CourseDTO course);

  void updateCourse(CourseDTO courseDTO);

  void enableCourse(String courseName);

  void deleteCourse(String courseId);

  void disableCourse(String courseName);

  List<StudentDTO> getEnrolledStudents(String courseName);

  void disenrollStudent(Long studentId, String courseId);

  void enrollStudent(Long studentId, String courseName);

  List<Boolean> enrollStudents(List<Long> studentIds, String courseName);

  List<Boolean> enrollStudentsCSV(Reader r, String courseName);

  List<TeamDTO> getTeamsForCourse(String courseName);

  List<StudentDTO> getEnrolledWithTeam(String courseName);

  List<StudentDTO> getEnrolledWithoutTeam(String courseName);
}
