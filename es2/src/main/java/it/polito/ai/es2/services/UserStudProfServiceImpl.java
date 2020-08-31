package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.*;
import it.polito.ai.es2.entities.Professor;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.ProfessorRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.UserRepository;
import it.polito.ai.es2.services.exceptions.FailedAddException;
import it.polito.ai.es2.services.exceptions.NullParameterException;
import it.polito.ai.es2.services.exceptions.UsernameAlreadyUsedException;
import it.polito.ai.es2.services.interfaces.UserStudProfService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Descrizione classe<p>Politica di sovrascrittura adottata: in quasi tutti i metodi add, se un id era già presente nel database non sovrascrivo i dati
 * già esistenti (tranne nel caso di proposeTeam, che poichè ha un id autogenerato, si è deciso di aggiornare il team vecchio usando
 * sempre la proposeTeam).
 */
@Service
@Transactional
@Log
public class UserStudProfServiceImpl implements UserStudProfService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  ProfessorRepository professorRepository;
  @Autowired
  UserRepository userRepository;
  // Bean created in WebConfig
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDTO addNewUser(@Valid UserDTO user) {
    if (userRepository.findTopByUsername(user.getUsername()) != null)
      throw new UsernameAlreadyUsedException(user.getUsername());
    User newUser = new User();
    newUser.setUsername(user.getUsername());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setRoles(UserDTO.convertStringsToRoles(user.getRoles()));
    newUser.setEnabled(false);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);
    newUser.setActivationToken(newUser.getActivationToken());
    System.out.println(newUser);
    return modelMapper.map(userRepository.save(newUser), UserDTO.class);
  }

  /**
   * Used by UserController
   */
  public boolean checkUser(String user, String pass) {
    if (user == null || pass == null)
      return false;
    User u = userRepository.findTopByUsername(user);
    return u != null && passwordEncoder.matches(pass, u.getPassword());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudents_RestController#getAllStudents()}
   */
  @Override
  public List<StudentDTO> getAllStudents() {
    log.info("getAllStudents()");
    return studentRepository.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudents_RestController#getStudent(Long)}
   */
  @Override
  public Optional<StudentDTO> getStudent(Long studentId) {
    log.info("getStudent(" + studentId + ")");
    return studentRepository.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
  }

  //  //  {"id":"S33","name":"S33-name","firstName":"S33-FirstName"}
//  // ---> Nella POST settare ContentType: application/json
//  @PostMapping()
//  public StudentDTO addStudent(@Valid @RequestBody StudentDTO studentDTO) {
//    userService.addStudent(studentDTO);
//    return modelHelper.enrich(studentDTO);
//  }
  @Override
  public StudentDTO addStudent(@Valid StudentDTO student) {
    log.info("addStudent(" + student + ")");
    if (student == null || student.getId() == null)
      throw new FailedAddException("null parameters");
    Student s = modelMapper.map(student, Student.class);
    if (studentRepository.existsById(student.getId())) {
      throw new FailedAddException("duplicate");
    }
    return modelMapper.map(studentRepository.save(s), StudentDTO.class);
  }

  @Override
  public ProfessorDTO addProfessor(@Valid ProfessorDTO professor) {
    log.info("addProfessor(" + professor + ")");
    if (professor == null || professor.getId() == null)
      throw new FailedAddException("null parameters");
    Professor s = modelMapper.map(professor, Professor.class);
    if (professorRepository.existsById(professor.getId())) {
      throw new FailedAddException("duplicate");
    }
    return modelMapper.map(professorRepository.save(s), ProfessorDTO.class);
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudents_RestController#getCourses(Long)}
   */
  @Override
  public List<CourseDTO> getEnrolledCourses(Long studentId) {
    log.info("getCourses(" + studentId + ")");
    if (studentId == null) throw new NullParameterException("student id");
    return studentRepository.getOne(studentId).getCourses().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudents_RestController#getTeamsForStudent(Long)}
   */
  @Override
  public List<TeamDTO> getTeamsForStudent(Long studentId) {
    log.info("getTeamsForStudent(" + studentId + ")");
    if (studentId == null) throw new NullParameterException("student id");
    return studentRepository.getOne(studentId).getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
}
