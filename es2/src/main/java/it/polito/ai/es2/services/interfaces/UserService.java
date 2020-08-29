package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.ProfessorDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
  List<StudentDTO> getAllStudents();

  Optional<StudentDTO> getStudent(Long studentId);

  void addStudent(StudentDTO student);

  List<CourseDTO> getEnrolledCourses(Long studentId);

  List<TeamDTO> getTeamsForStudent(Long studentId);

  boolean addProfessor(ProfessorDTO student);
  
//  void createPasswordResetTokenForUser(final UserDAO user, final String token);
//  String validatePasswordResetToken(String token);
//  Optional<UserDAO> getUserByPasswordResetToken(final String token);
//  void changeUserPassword(UserDAO user, String password);
//  void insertAdmin();
}
