package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.UserDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface UserStudProfService {
  UserDTO addNewUser(@Valid UserDTO userDTO);

  boolean confirmUser(@NotBlank String token);

  StudentDTO addStudent(@Valid StudentDTO studentDTO);

  List<StudentDTO> getAllStudents();

  Optional<StudentDTO> getStudent(Long studentId);
}
