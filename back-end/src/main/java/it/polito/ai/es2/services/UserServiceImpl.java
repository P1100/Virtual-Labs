package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.ProfessorDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.UserDTO;
import it.polito.ai.es2.entities.*;
import it.polito.ai.es2.repositories.*;
import it.polito.ai.es2.services.exceptions.FailedAddException;
import it.polito.ai.es2.services.exceptions.UserAlreadyEnabledException;
import it.polito.ai.es2.services.exceptions.UsernameAlreadyUsedException;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.UserService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Log
@Validated
public class UserServiceImpl implements UserService {
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
  @Autowired
  NotificationService notificationService;
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
    newUser.setEnabled(false);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);
    newUser.setTypeUser(userDTO.getTypeUser());
    User savedUser = userRepository.save(newUser);

    if (userDTO.getTypeUser() == User.TypeUser.STUDENT) {
      StudentDTO studentDTO = modelMapper.map(userDTO, StudentDTO.class);
      studentDTO.setId(Long.valueOf(userDTO.getUsername()));
      addStudent(studentDTO); // imageId in studentDTO, mapped from userDTO
    } else {
      ProfessorDTO professorDTO = modelMapper.map(userDTO, ProfessorDTO.class);
      professorDTO.setId(Long.valueOf(userDTO.getUsername()));
      addProfessor(professorDTO); // imageId in professorDTO, mapped from userDTO
    }
    /* NOTIFY USER */
    Token token = new Token();
    token.setId((UUID.randomUUID().toString().toLowerCase()));
    token.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusHours(2400)));
    token.addSetUser(savedUser);
    Token token1 = tokenRepository.save(token);

    StringBuffer sb = new StringBuffer();
    sb.append("Hello ").append(userDTO.getFirstName() + ' ' + userDTO.getLastName() + " - " + userDTO.getUsername());
    sb.append("\n\nLink to confirm registration:\n" + baseUrl + "/notification/user/confirm/" + token.getId());
    String mymatricola = environment.getProperty("mymatricola");
    System.out.println(sb);
//    notificationService.sendMessage("s" + mymatricola + "@studenti.polito.it", "[User:" + userDTO.getUsername() + "] Virtual Labs email verification", sb.toString());
    return modelMapper.map(savedUser, UserDTO.class);
  }

  @Override public boolean confirmUser(@NotBlank String token) {
    Optional<Token> tokenOptional = tokenRepository.findById(token);
    if (tokenOptional.isEmpty()) {
      log.warning("Token not found, or expired: " + token);
      return false;
    }
    Optional<User> userOptional = tokenOptional.map(Token::getUser);
    if (userOptional.isEmpty()) {
      log.warning("No user associated with token " + token);
      return false;
    }
    if (userOptional.get().isEnabled()) {
      throw new UserAlreadyEnabledException(userOptional.get().getUsername());
    }
    if (LocalDateTime.now().isAfter(tokenOptional.get().getExpiryDate().toLocalDateTime())) {
      userOptional.get().setEnabled(false);
      userOptional.get().setTokenSignup(null);
      tokenRepository.delete(tokenOptional.get());
      return false;
    }
    userOptional.get().setEnabled(true);
    userOptional.get().setTokenSignup(null);
    tokenRepository.delete(tokenOptional.get());
    return true;
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
      img.ifPresent(s::addImage);
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
      img.ifPresent(p::addImage);
    }
    return modelMapper.map(professorRepository.save(p), ProfessorDTO.class);
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudents_RestController#getAllStudents()}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public List<StudentDTO> getAllStudents() {
    log.info("getAllStudents()");
    return studentRepository.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudents_RestController#getStudent(Long)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public Optional<StudentDTO> getStudent(Long studentId) {
    log.info("getStudent(" + studentId + ")");
    return studentRepository.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
  }
}
