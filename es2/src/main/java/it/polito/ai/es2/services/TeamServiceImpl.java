package it.polito.ai.es2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.es2.controllers.CourseRestController;
import it.polito.ai.es2.domains.StudentViewModel;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.exceptions.*;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Politica di sovrascrittura adottata: in quasi tutti i metodi add, se un id era già presente nel database non sovrascrivo i dati
 * già esistenti (tranne nel caso di proposeTeam, che poichè ha un id autogenerato, si è deciso di aggiornare il team vecchio usando
 * sempre la proposeTeam).
 */
@Service
@PreAuthorize("hasRole('ADMIN') or hasRole('admin') or hasRole('STUDENT') or hasRole('PROFESSOR') ")
//@PreAuthorize("authenticated")
@Transactional
@Log
public class TeamServiceImpl implements TeamService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  TokenRepository tokenRepository;
  @Autowired
  NotificationService notificationService;
  
  /**
   * GET {@link CourseRestController#getAllCourses()}
   */
  @Override
  public List<CourseDTO> getAllCourses() {
    return courseRepository.findAll().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<StudentDTO> getEnrolledStudents(String courseName) throws CourseNotFoundException {
    if (courseName == null) throw new TeamServiceException("getEnrolledStudents() - null parameters");
    if (!courseRepository.existsById(courseName))
      throw new CourseNotFoundException("getEnrolledStudents() - StudentNotFoundException: (" + courseName + ")");
    Course c = courseRepository.getOne(courseName);
    return c.getStudents().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<TeamDTO> getTeamsForCourse(String courseName) {
    if (courseName == null) throw new TeamServiceException("null parameter");
    Optional<Course> co = courseRepository.findById(courseName);
    if (!co.isPresent()) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return co.get().getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * POST {@link it.polito.ai.es2.controllers.StudentRestController#addStudent(StudentDTO)}
   */
// @PreAuthorize("hasRole('ROLE_PROFESSOR') or @securityServiceImpl.hasPermissions()")
  //  @PreAuthorize("hasRole('ROLE_PROFESSOR') or @securityServiceImpl.hasPermissions()")
//  @Secured({"ROLE_VIEWER", "ROLE_EDITOR", "ROLE_professor"})
//  @RolesAllowed({ "ROLE_VIEWER", "ROLE_EDITOR","ROLE_professor"})
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
//  @PreAuthorize("#username == authentication.principal.username")
//  @PreAuthorize("hasRole('ROLE_VIEWER') or hasRole('ROLE_EDITOR')")
//  @PreAuthorize("#username == authentication.principal.username")
//  @PreAuthorize("#username == authentication.principal.username")
//  @PostAuthorize("returnObject.username == authentication.principal.nickName")
//  public CustomUser securedLoadUserDetail(String username) {
  @Override
  public boolean addStudent(StudentDTO student) {
    if (student == null || student.getId() == null) return false;
    Student s = modelMapper.map(student, Student.class);
    try {
      if (!studentRepository.existsById(student.getId())) {
        studentRepository.save(s);
        return true;
      }
      return false;
    } catch (IllegalArgumentException e) {
      log.warning("###### IllegalArgumentException:" + e.toString());
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      log.warning("###### Other Exception:" + e.toString());
      e.printStackTrace();
      return false;
    }
  }
  
  /**
   * POST {@link it.polito.ai.es2.controllers.CourseRestController#addCourse(CourseDTO)}
   */
  @Override
  public boolean addCourse(CourseDTO course) {
    if (course == null || course.getIdname() == null) return false;
    Course c = modelMapper.map(course, Course.class);
    try {
      if (!courseRepository.existsById(course.getIdname())) {
        courseRepository.save(c);
        return true;
      }
      return false;
    } catch (IllegalArgumentException e) {
      log.warning("IllegalArgumentException:" + e.toString());
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      log.warning("###### Other Exception:" + e.toString());
      e.printStackTrace();
      return false;
    }
  }
  
  /**
   * POST {@link it.polito.ai.es2.controllers.CourseRestController#addAll(List<StudentDTO>)}
   */
  @Override
  public List<Boolean> addAll(List<StudentDTO> students) {
    if (students == null) throw new TeamServiceException("null parameters");
    return students.stream()
               .map(x -> modelMapper.map(x, Student.class))
               .peek(e -> log.info("addAll(List<StudentDTO> - size:" + students.size() + " - Entità corrente:" + e))
               .map(
                   e -> {
                     boolean b = studentRepository.existsById(e.getId());
                     // Se studente esisteva già...
                     if (b)
                       return Boolean.FALSE;
                     // Se invece non esisteva...
                     studentRepository.save(e);
                     return Boolean.TRUE;
                   }).collect(Collectors.toList());
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public Optional<CourseDTO> getCourse(String name) {
    if (name == null) throw new TeamServiceException("getCourse() - null parameter");
    return courseRepository.findById(name).map(x -> modelMapper.map(x, CourseDTO.class));
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public Optional<StudentDTO> getStudent(String studentId) {
    if (studentId == null) throw new TeamServiceException("getStudent() - null parameter");
    return studentRepository.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<CourseDTO> getCourses(String studentId) {
    if (studentId == null) throw new TeamServiceException("null parameters");
    return studentRepository.getOne(studentId).getCourses().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.StudentRestController#getAllStudents()}
   */
  @Override
  public List<StudentDTO> getAllStudents() {
    return studentRepository.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  @PreAuthorize("@securityServiceImpl.hasPermissions(authentication.principal.username, #courseName)")
  public void enableCourse(String courseName) throws CourseNotFoundException {
    if (courseName == null) throw new CourseNotFoundException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("course not found");
    
    courseRepository.getOne(courseName).setEnabled(true);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public void disableCourse(String courseName) throws CourseNotFoundException {
    if (courseName == null) throw new CourseNotFoundException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("course not found");
    
    courseRepository.getOne(courseName).setEnabled(false);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public boolean addStudentToCourse(String studentId, String courseName) throws StudentNotFoundException, CourseNotFoundException {
    if (studentId == null || courseName == null) throw new TeamServiceException("addStudentToCourse() - null parameters");
    if (!studentRepository.existsById(studentId)) throw new StudentNotFoundException("addStudentToCourse() - student not found:" + studentId);
    Optional<Course> courseOptional = courseRepository.findById(courseName);
    if (!courseOptional.isPresent()) throw new CourseNotFoundException("addStudentToCourse() - course not found:" + courseName);
    
    Course c = courseOptional.get();
    // Controllo che corso sia enabled
    if (!c.isEnabled())
      return false;
    Student s = studentRepository.getOne(studentId);
    // Controllo che studente non sia già iscritto al corso
    if (c.getStudents().stream().anyMatch(x -> x.getId().equals(studentId)))
      return false;
    
    log.info("addStudentToCourse ---> BEFORE ADD");
    c.addStudent(s);
    log.info("addStudentToCourse(" + studentId + "," + courseName + "). " + c.getIdname() + "->ListStudents: " + c.getStudents());
    return true;
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<Boolean> enrollAll(List<String> studentIds, String courseName) {
    if (studentIds == null || courseName == null)
      throw new TeamServiceException("enrollAll(List<String> studentIds, String courseName) - null parameters");
    if (!courseRepository.existsById(courseName))
      throw new CourseNotFoundException("enrollAll(List<String> studentIds, String courseName) - course not found");
    
    Course course = courseRepository.getOne(courseName);
    List<Boolean> lb = new ArrayList<>();
    for (String id : studentIds) {
      if (id == null) { // null was put by AddAndReroll function
        lb.add(false);
        continue;
      }
      if (!studentRepository.existsById(id))
        throw new StudentNotFoundException("enrollAll(List<String> studentIds, String courseName) - student in list not found");
      Student student = studentRepository.getOne(id);
      // Controllo che lo studente corrente (id) non sià già presente nella lista degli studenti iscritti al corso
      if (course.getStudents().stream().anyMatch(x -> x.getId().equals(id))) {
        lb.add(false);
        continue;
      }
      lb.add(true);
      course.addStudent(student);
//      student.addCourse(course); // -> creerei duplicati
    }
    log.info("enrollAll List<Boolean> ritornata:" + lb);
    return lb;
  }
  /**
   * Prendo il CSV dal controller (implementato)
   *  {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<Boolean> addAndEroll(Reader reader, String courseName) {
    if (reader == null || courseName == null) throw new TeamServiceException("addAndEroll(Reader reader, String courseName) - null parameters");
    Optional<Course> courseOptional = courseRepository.findById(courseName);
    if (!courseOptional.isPresent()) throw new CourseNotFoundException("addAndEroll(Reader reader, String courseName) - course not found");
  
    CsvToBean<StudentViewModel> csvToBean = new CsvToBeanBuilder(reader)
                                                .withType(StudentViewModel.class)
                                                .withIgnoreLeadingWhiteSpace(true)
                                                .build();
    List<StudentViewModel> users = csvToBean.parse();
    log.info(String.valueOf(users));
    List<String> list_ids = users.stream()
                                .map(new_studentViewModel -> modelMapper.map(new_studentViewModel, StudentDTO.class))
                                .map(new_studentDTO ->
                                {
                                  Optional<Student> optionalStudent_fromDb = studentRepository.findById(new_studentDTO.getId());
                                  if (optionalStudent_fromDb.isPresent())
                                    return null;
                                  Student newStudent = modelMapper.map(new_studentDTO, Student.class);
                                  return studentRepository.save(newStudent);
                                })
                                // No perchè devo elaborare ordine in enrollAll
//                                .filter(k -> {
//                                    if (k == null)
//                                      return false;
//                                    else
//                                      return true;
//                                  })
                                .map(y -> y != null ? y.getId() : null).collect(Collectors.toList());
    log.info(courseName + " - CSV_Valid_Students: " + list_ids);
    return enrollAll(list_ids, courseName);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<TeamDTO> getTeamsForStudent(String studentId) {
    if (studentId == null) throw new TeamServiceException("getTeamsForStudent() - null parameters");
    return studentRepository.getOne(studentId).getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<StudentDTO> getMembers(Long teamId) {
    if (teamId == null) throw new TeamServiceException("getMembers() - null parameters");
    Optional<Team> team = teamRepository.findById(teamId);
    if (team.isPresent())
      return team.get().getMembers().stream().filter(Objects::nonNull).map(y -> modelMapper.map(y, StudentDTO.class)).collect(Collectors.toList());
    else
      throw new TeamServiceException("getMembers() - team not found");
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  @Override
  public List<StudentDTO> getStudentsInTeams(String courseName) {
    if (courseName == null) throw new TeamServiceException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return courseRepository.getStudentsInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.}
   */
  //  @PreAuthorize("hasRole('ROLE_AUTHENTICATED')")
  @Override
  public List<StudentDTO> getAvailableStudents(String courseName) {
    if (courseName == null) throw new TeamServiceException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return courseRepository.getStudentsNotInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * Se team già presente (stesso nome-corso), allora lo cancello e poi reinserisco il nuovo. Si potrebbe anche aggiornare campi senza cancellare
   * Metodo complicato. Il problema è cosa fare se si fà il propose di un nuovo team, che abbia lo stesso nome di uno già presente sullo
   * stesso corso, con il vecchio/i che può essere già stato validato.
   * Per semplificare, si è deciso di:
   * 1-Non si permette la creazione di un team duplicato sullo stesso corso, prima bisogna eliminarlo manualmente tramite email reject da parte di uno
   * qualsiasi degli appartenenti al team.
   * 2-Se  team era già stato attivato, diventa definitivo e non si può più cancellare tramite app
   */
  @Override
  public TeamDTO proposeTeam(String courseIdname, String team_name, List<String> memberIds) throws TeamServiceException {
    if (courseIdname == null || team_name == null || memberIds == null) throw new TeamServiceException("null parameter");
    Optional<Course> oc = courseRepository.findById(courseIdname);
    if (!oc.isPresent()) throw new CourseNotFoundException("proposeTeam - course not found");
    if (!oc.get().isEnabled()) throw new CourseNotEnabledException("proposeTeam() - course not enabled");
    List<Optional<Student>> streamopt_listStudentsProposal = memberIds.stream().map(x -> studentRepository.findById(x)).collect(Collectors.toList());
    if (!streamopt_listStudentsProposal.stream().allMatch(Optional::isPresent))
      throw new StudentNotFoundException("proposeTeam() - student not found");
    
    Course course = oc.get();
    List<Student> listStudentsProposal = streamopt_listStudentsProposal.stream().map(Optional::get).collect(Collectors.toList());
    if (!course.getStudents().containsAll(listStudentsProposal)) // !listStudentsProposal.stream().allMatch(x -> course.getStudents().contains(x))
      throw new StudentNotEnrolledException("proposeTeam() - non tutti gli studenti sono iscritti al corso " + course.getIdname());
    // Controllo se tra gli studenti dei vari teams del corso, ce n'è qualcuno tra quelli presenti nella proposta
    if (course.getTeams().size() != 0 &&
            !course.getTeams()
                 .stream()
                 .map(Team::getMembers)
                 .flatMap(List::stream)
                 .distinct()
                 .noneMatch(student -> {
                   return listStudentsProposal.stream().anyMatch(student::equals);
                 })
    )
      throw new StudentInMultipleTeamsException("proposeTeam() - studenti fanno parte di altri gruppi nell’ambito dello stesso corso");
    if (listStudentsProposal.size() < course.getMin() || listStudentsProposal.size() > course.getMax())
      throw new CourseCardinalConstrainsException("proposeTeam() - non rispettati i vincoli di cardinalità del corso su dimensioni team");
    if (!listStudentsProposal.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException("proposeTeam() - duplicati nell'elenco dei partecipanti della proposta team");
    if (teamRepository.findFirstByNameAndCourse_Idname(team_name, courseIdname) != null)
      throw new TeamAlreayCreatedException("proposeTeam() - team già creato");
    
    TeamDTO teamDTO = new TeamDTO(null, team_name, Team.status_inactive());
    Team new_team = modelMapper.map(teamDTO, Team.class);
    // aggiungo nuovo team, a studenti e al corso
    for (Student student : new ArrayList<>(listStudentsProposal)) {
      student.addTeam(new_team); // add su studenti
    }
    course.addTeam(new_team); // add sul singolo corso
    TeamDTO return_teamDTO = modelMapper.map(teamRepository.save(new_team), TeamDTO.class);
    notificationService.notifyTeam(return_teamDTO, memberIds);
    return return_teamDTO;
  }
  
  @Override
  public boolean evictTeam(Long teamId) {
    Optional<Team> optionalTeam = teamRepository.findById(teamId);
    if (!optionalTeam.isPresent())
      return false;
    Team team_to_delete = optionalTeam.get();
    
    for (Student student : team_to_delete.getMembers()) {
      // usare "student.removeTeam()" rimuoverebbe studenti da team, il che creerebbe problemi in quanto modificherebbe il ciclo foreach enhanced in corso (java.util.ConcurrentModificationException)
      student.getTeams().remove(team_to_delete);
    }
    // --> non serve rimuovere students e course da team, perchè tanto lo cancello
    team_to_delete.getCourse().getTeams().remove(team_to_delete);
    teamRepository.delete(team_to_delete);
    return true;
  }
  
  /**
   * @param status - Team.status_active() or Team.status_inactive()
   */
  @Override
  public boolean setTeamStatus(Long teamId, int status) {
    Team team = teamRepository.findById(teamId).orElse(null);
    if (team == null || team.getStatus() == status)
      return false;
    team.setStatus(status); // no need to save, will be flushed automatically at the end of transaction (since not a new entity)
    return true;
  }
  
  //  -------------------------------------- PRIVATE METHODS -----------------------------------
/*
  private Optional<TeamDTO> getTeamDTOfromId(Long teamId) {
    Optional<Team> team = teamRepository.findById(teamId);//.orElse(null);
    Optional<TeamDTO> teamDTO = team.map(x -> modelMapper.map(x, TeamDTO.class));
    return teamDTO;
  }*/
  private boolean isTeamCreatedAndActive(Long teamId) {
    Team team = teamRepository.findById(teamId).orElse(null);
    return team != null && team.getStatus() != Team.status_inactive();
  }
  
  // TODO: trovare metodo migliore per trovare teams con stessi dati (equals exclude e repository? new query? streams?)
  private Team getTeamFromDTO(TeamDTO teamDTO) {
    return null;
  }
}
