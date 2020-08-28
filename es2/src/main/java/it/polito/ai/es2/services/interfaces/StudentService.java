package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {
  List<StudentDTO> getAllStudents();

  Optional<StudentDTO> getStudent(Long studentId);

  boolean addStudent(StudentDTO student);

  List<CourseDTO> getEnrolledCourses(Long studentId);

  List<TeamDTO> getTeamsForStudent(Long studentId);
}
