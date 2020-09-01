package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.dtos.UserDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserStudProfService {
  UserDTO addNewUser(UserDTO userDTO);

  StudentDTO addStudent(@Valid StudentDTO studentDTO);

  List<StudentDTO> getAllStudents();

  Optional<StudentDTO> getStudent(Long studentId);

  List<CourseDTO> getEnrolledCourses(Long studentId);

  List<TeamDTO> getTeamsForStudent(Long studentId);


//  void createPasswordResetTokenForUser(final UserDAO user, final String token);
//  String validatePasswordResetToken(String token);
//  Optional<UserDAO> getUserByPasswordResetToken(final String token);
//  void changeUserPassword(UserDAO user, String password);
//  void insertAdmin();
}
