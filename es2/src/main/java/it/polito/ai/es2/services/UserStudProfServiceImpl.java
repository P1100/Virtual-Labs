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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
  private PasswordEncoder passwordEncoder;   // Bean created in WebConfig
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
  @Autowired
  public TokenRepository tokenRepository;
  //  @Autowired
//  private NotificationService notificationService;
  @Value("${server.port}")
  private String port;
  @Value("${server.address}")
  private String address;
  @Value("${myprop.prefixurl}")
  private String httpPrefix; // http or https
  public String baseUrl = "";
  @Autowired
  Environment environment;

  @PostConstruct
  public void init() {
    baseUrl = httpPrefix + "://" + address + ":" + port;
  }

  @Override
  public UserDTO addNewUser(@Valid UserDTO userDTO) {
    if (userRepository.findTopByUsername(userDTO.getUsername()) != null)
      throw new UsernameAlreadyUsedException(userDTO.getUsername());
    User newUser = new User();
    newUser.setUsername(userDTO.getUsername());
    newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    newUser.setRoles(UserDTO.convertStringsToRoles(userDTO.getRoles()));
    newUser.setEnabled(true); // fales
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);
    newUser.setTypeUser(userDTO.getTypeUser());
    System.out.println(newUser);
    User savedUser = userRepository.save(newUser);
    System.out.println(savedUser);

    if (userDTO.getTypeUser() == User.TypeUser.STUDENT) {
      StudentDTO studentDTO = modelMapper.map(userDTO, StudentDTO.class);
      studentDTO.setId(Long.valueOf(userDTO.getUsername()));
      System.out.println(studentDTO);
      addStudent(studentDTO);
    } else {
      ProfessorDTO professorDTO = modelMapper.map(userDTO, ProfessorDTO.class);
      professorDTO.setId(Long.valueOf(userDTO.getUsername()));
      System.out.println(professorDTO);
      addProfessor(professorDTO);
    }
//    notificationService.notifyUser(userDTO);
    return modelMapper.map(savedUser, UserDTO.class);
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

  private ProfessorDTO addProfessor(@Valid ProfessorDTO professorDTO) {
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
