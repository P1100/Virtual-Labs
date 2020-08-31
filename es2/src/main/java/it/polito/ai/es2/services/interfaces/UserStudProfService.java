package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserStudProfService {
  UserDTO addNewUser(@Valid UserDTO user);

  List<StudentDTO> getAllStudents();

  Optional<StudentDTO> getStudent(Long studentId);

  StudentDTO addStudent(StudentDTO student);

  List<CourseDTO> getEnrolledCourses(Long studentId);

  List<TeamDTO> getTeamsForStudent(Long studentId);

  ProfessorDTO addProfessor(ProfessorDTO student);

//  void createPasswordResetTokenForUser(final UserDAO user, final String token);
//  String validatePasswordResetToken(String token);
//  Optional<UserDAO> getUserByPasswordResetToken(final String token);
//  void changeUserPassword(UserDAO user, String password);
//  void insertAdmin();
}
