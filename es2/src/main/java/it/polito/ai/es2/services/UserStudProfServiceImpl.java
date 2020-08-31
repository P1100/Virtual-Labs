package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.*;
import it.polito.ai.es2.entities.Image;
import it.polito.ai.es2.entities.Professor;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.*;
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
  @Autowired
  ImageRepository imageRepository;
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

  @Override
  public StudentDTO addStudent(@Valid StudentDTO studentDTO) {
    log.info("addStudent(" + studentDTO + ")");
    if (studentDTO == null || studentDTO.getId() == null)
      throw new FailedAddException("null parameters");
    Student s = modelMapper.map(studentDTO, Student.class);
    if (studentRepository.existsById(studentDTO.getId())) {
      throw new FailedAddException("duplicate");
    }
    if (studentDTO.getImageId() != null) {
      Optional<Image> img = imageRepository.findById(studentDTO.getImageId());
      if (img.isPresent())
        s.addImage(img.get());
    }
    return modelMapper.map(studentRepository.save(s), StudentDTO.class);
  }

  @Override
  public ProfessorDTO addProfessor(@Valid ProfessorDTO professorDTO) {
    log.info("addProfessor(" + professorDTO + ")");
    if (professorDTO == null || professorDTO.getId() == null)
      throw new FailedAddException("null parameters");
    Professor p = modelMapper.map(professorDTO, Professor.class);
    if (professorRepository.existsById(professorDTO.getId())) {
      throw new FailedAddException("duplicate");
    }
    if (professorDTO.getImageId() != null) {
      Optional<Image> img = imageRepository.findById(professorDTO.getImageId());
      if (img.isPresent())
        p.addImage(img.get());
    }
    return modelMapper.map(professorRepository.save(p), ProfessorDTO.class);
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
