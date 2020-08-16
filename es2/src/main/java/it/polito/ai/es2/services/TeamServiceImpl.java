package it.polito.ai.es2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.es2.controllers.APICourses_RestController;
import it.polito.ai.es2.controllers.APITeams_RestController;
import it.polito.ai.es2.domains.StudentViewModel;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.repositories.*;
import it.polito.ai.es2.services.exceptions.*;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Descrizione classe<p>Politica di sovrascrittura adottata: in quasi tutti i metodi add, se un id era già presente nel database non sovrascrivo i dati
 * già esistenti (tranne nel caso di proposeTeam, che poichè ha un id autogenerato, si è deciso di aggiornare il team vecchio usando
 * sempre la proposeTeam).
 */
@Service
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
  @Autowired
  AssignmentRepository assignmentRepository;
  @Autowired
  ImageRepository imageRepository;
  @Autowired
  HomeworkRepository homeworkRepository;
  @Autowired
  VMRepository vmRepository;
  
  @Override
  public void testing(
  ) {
  
  }
  
  /**
   * GET {@link APICourses_RestController#getAllCourses()}
   */
  @Override
  public List<CourseDTO> getAllCourses() {
    log.info("getAllCourses");
    return courseRepository.findAll().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getCourse(String)}
   */
  @Override
  public Optional<CourseDTO> getCourse(String courseName) {
    log.info("getCourse(" + courseName + ")");
    if (courseName == null) throw new TeamServiceException("getCourse() - null parameter");
    return courseRepository.findById(courseName).map(x -> modelMapper.map(x, CourseDTO.class));
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getEnrolledStudents(String)}
   */
  @Override
  public List<StudentDTO> getEnrolledStudents(String courseId) throws CourseNotFoundException {
    log.info("getEnrolledStudents(" + courseId + ")");
    if (courseId == null) throw new TeamServiceException("getEnrolledStudents() - null parameters");
    if (!courseRepository.existsById(courseId))
      throw new CourseNotFoundException("Course not found! " + courseId);
    Course c = courseRepository.getOne(courseId);
    return c.getStudents().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getStudentsInTeams(String)}
   */
  @Override
  public List<StudentDTO> getStudentsInTeams(String courseName) {
    log.info("getStudentsInTeams(" + courseName + ")");
    if (courseName == null) throw new TeamServiceException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return courseRepository.getStudentsInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getAvailableStudents(String)}
   */
  @Override
  public List<StudentDTO> getAvailableStudents(String courseName) {
    log.info("getAvailableStudents(" + courseName + ")");
    if (courseName == null) throw new TeamServiceException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return courseRepository.getStudentsNotInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getTeamsForCourse(String)}
   */
  @Override
  public List<TeamDTO> getTeamsForCourse(String courseName) {
    log.info("getTeamsForCourse(" + courseName + ")");
    if (courseName == null) throw new TeamServiceException("null parameter");
    Optional<Course> co = courseRepository.findById(courseName);
    if (!co.isPresent()) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return co.get().getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getAllStudents()}
   */
  @Override
  public List<StudentDTO> getAllStudents() {
    log.info("getAllStudents()");
    return studentRepository.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getStudent(String)}
   */
  @Override
  public Optional<StudentDTO> getStudent(String studentId) {
    log.info("getStudent(" + studentId + ")");
    if (studentId == null) throw new TeamServiceException("getStudent() - null parameter");
    return studentRepository.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getCourses(String)}
   */
  @Override
  public List<CourseDTO> getCourses(String studentId) {
    log.info("getCourses(" + studentId + ")");
    if (studentId == null) throw new TeamServiceException("null parameters");
    return studentRepository.getOne(studentId).getCourses().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getTeamsForStudent(String)}
   */
  @Override
  public List<TeamDTO> getTeamsForStudent(String studentId) {
    log.info("getTeamsForStudent(" + studentId + ")");
    if (studentId == null) throw new TeamServiceException("getTeamsForStudent() - null parameters");
    return studentRepository.getOne(studentId).getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link APITeams_RestController#getAllTeams()}
   */
  @Override
  public List<TeamDTO> getAllTeams() {
    log.info("getAllTeams()");
    return teamRepository.findAll().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getTeam(Long)}
   */
  @Override
  public Optional<TeamDTO> getTeam(Long teamId) {
    log.info("getTeam(" + teamId + ")");
    if (teamId == null) throw new TeamServiceException("getTeam() - null parameter");
    return teamRepository.findById(teamId).map(x -> modelMapper.map(x, TeamDTO.class));
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
   */
  @Override
  public List<StudentDTO> getMembers(Long teamId) {
    log.info("getMembers(" + teamId + ")");
    if (teamId == null) throw new TeamServiceException("getMembers() - null parameters");
    Optional<Team> team = teamRepository.findById(teamId);
    if (team.isPresent())
      return team.get().getMembers().stream().filter(Objects::nonNull).map(y -> modelMapper.map(y, StudentDTO.class)).collect(Collectors.toList());
    else
      throw new TeamServiceException("getMembers() - team not found");
  }
  
  /**
   * POST {@link it.polito.ai.es2.controllers.APICourses_RestController#addCourse(CourseDTO)}
   */
  @Override
  public boolean addCourse(CourseDTO course) {
    log.info("addCourse(" + course + ")");
    if (course == null || course.getId() == null) return false;
    Course c = modelMapper.map(course, Course.class);
    try {
      if (!courseRepository.existsById(course.getId())) {
        courseRepository.save(c);
        return true;
      }
      return false;
    } catch (IllegalArgumentException e) {
      log.warning("IllegalArgumentException:" + e.toString());
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      log.warning("# Other Exception:" + e.toString());
      e.printStackTrace();
      return false;
    }
  }
  
  /**
   * POST {@link it.polito.ai.es2.controllers.APIStudent_RestController#addStudent(StudentDTO)}
   */
  @Override
  public boolean addStudent(StudentDTO student) {
    log.info("addStudent(" + student + ")");
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
   * POST {@link it.polito.ai.es2.controllers.APIStudent_RestController#addAll(List<StudentDTO>)}
   */
  @Override
  public List<Boolean> addAllStudents(List<StudentDTO> students) {
    log.info("addAll(" + students + ")");
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
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enableCourse(String)}
   */
  @Override
  public void enableCourse(String courseName) {
    log.info("enableCourse(" + courseName + ")");
    if (courseName == null) throw new CourseNotFoundException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("course not found");
    
    courseRepository.getOne(courseName).setEnabled(true);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#disableCourse(String)}
   */
  @Override
  public void disableCourse(String courseName) {
    log.info("disableCourse(" + courseName + ")");
    if (courseName == null) throw new CourseNotFoundException("null parameter");
    if (!courseRepository.existsById(courseName)) throw new CourseNotFoundException("course not found");
    
    courseRepository.getOne(courseName).setEnabled(false);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#disenrollStudent(String, String)}
   */
  @Override
  public void disenrollStudent(String studentId, String courseId) {
    log.info("disenrollStudent(" + studentId + ", " + courseId + ")");
    if (studentId == null || courseId == null) throw new TeamServiceException("disenrollStudent() - null parameters");
    if (!studentRepository.existsById(studentId)) throw new StudentNotFoundException("disenrollStudent() - student not found:" + studentId);
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (!courseOptional.isPresent()) throw new CourseNotFoundException("disenrollStudent() - course not found:" + courseId);
    
    Course c = courseOptional.get();
    Student s = studentRepository.getOne(studentId);
    c.removeDisenrollStudent(s);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudent(String, Map)}
   */
  @Override
  public boolean enrollStudent(String studentId, String courseId) {
    log.info("disenrollStudent(" + studentId + ", " + courseId + ")");
    if (studentId == null || courseId == null) throw new TeamServiceException("enrollStudent() - null parameters");
    if (!studentRepository.existsById(studentId)) throw new StudentNotFoundException("enrollStudent() - student not found:" + studentId);
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (!courseOptional.isPresent()) throw new CourseNotFoundException("enrollStudent() - course not found:" + courseId);
    
    Course c = courseOptional.get();
    // Controllo che corso sia enabled
    if (!c.isEnabled())
      return false;
    Student s = studentRepository.getOne(studentId);
    // Controllo che studente non sia già iscritto al corso
    if (c.getStudents().stream().anyMatch(x -> x.getId().equals(studentId)))
      return false;
    
    c.addEnrollStudent(s);
    return true;
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudents(List, String)}
   */
  @Override
  public List<Boolean> enrollStudents(List<String> studentIds, String courseId) {
    log.info("enrollStudents(" + studentIds + ", " + courseId + ")");
    if (studentIds == null || courseId == null)
      throw new TeamServiceException("enrollStudents(List<String> studentIds, String courseId) - null parameters");
    if (!courseRepository.existsById(courseId))
      throw new CourseNotFoundException("enrollStudents(List<String> studentIds, String courseId) - course not found");
    
    Course course = courseRepository.getOne(courseId);
    List<Boolean> lb = new ArrayList<>();
    for (String id : studentIds) {
      if (id == null) { // null was put by AddAndReroll function
        lb.add(false);
        continue;
      }
      if (!studentRepository.existsById(id))
        throw new StudentNotFoundException("enrollStudents(List<String> studentIds, String courseId) - student in list not found");
      Student student = studentRepository.getOne(id);
      // Controllo che lo studente corrente (id) non sià già presente nella lista degli studenti iscritti al corso
      if (course.getStudents().stream().anyMatch(x -> x.getId().equals(id))) {
        lb.add(false);
        continue;
      }
      lb.add(true);
      course.addEnrollStudent(student);
//      student.addCourse(course); // -> creerei duplicati
    }
    log.info("enrollStudents List<Boolean> ritornata:" + lb);
    return lb;
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudentsCSV(String, MultipartFile)}
   */
  @Override
  public List<Boolean> addAndEroll(Reader reader, String courseName) {
    log.info("addAndEroll(" + reader + ", " + courseName + ")");
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
                                .map(y -> y != null ? y.getId() : null).collect(Collectors.toList());
    log.info(courseName + " - CSV_Valid_Students: " + list_ids);
    return enrollStudents(list_ids, courseName);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#proposeTeam(String, String, List)}
   */
  // TODO: credo di aver fatto in modo che non ci possono essere piú gruppi con lo stesso nome per lo stesso corso, check in fase di creazione
  @Override
  public TeamDTO proposeTeam(String courseName, String team_name, List<String> memberIds) {
    log.info("proposeTeam(" + courseName + ", " + team_name + ", " + memberIds + ")");
    if (courseName == null || team_name == null || memberIds == null) throw new TeamServiceException("null parameter");
    Optional<Course> oc = courseRepository.findById(courseName);
    if (!oc.isPresent()) throw new CourseNotFoundException("proposeTeam - course not found");
    if (!oc.get().isEnabled()) throw new CourseNotEnabledException("proposeTeam() - course not enabled");
    List<Optional<Student>> streamopt_listStudentsProposal = memberIds.stream().map(x -> studentRepository.findById(x)).collect(Collectors.toList());
    if (!streamopt_listStudentsProposal.stream().allMatch(Optional::isPresent))
      throw new StudentNotFoundException("proposeTeam() - student not found");
    
    Course course = oc.get();
    List<Student> listStudentsProposal = streamopt_listStudentsProposal.stream().map(Optional::get).collect(Collectors.toList());
    if (!course.getStudents().containsAll(listStudentsProposal)) // !listStudentsProposal.stream().allMatch(x -> course.getStudents().contains(x))
      throw new StudentNotEnrolledException("proposeTeam() - non tutti gli studenti sono iscritti al corso " + course.getId());
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
    if (listStudentsProposal.size() < course.getMinEnrolled() || listStudentsProposal.size() > course.getMaxEnrolled())
      throw new CourseCardinalConstrainsException("proposeTeam() - non rispettati i vincoli di cardinalità del corso su dimensioni team");
    if (!listStudentsProposal.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException("proposeTeam() - duplicati nell'elenco dei partecipanti della proposta team");
    if (teamRepository.findFirstByNameAndCourse_id(team_name, courseName) != null)
      throw new TeamAlreayCreatedException("proposeTeam() - team già creato");

//    TeamDTO teamDTO = new TeamDTO(null, team_name, Team.status_inactive());
    TeamDTO teamDTO = TeamDTO.builder().id(null).name(team_name).status(Team.status_inactive()).build();
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
  
  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#evictTeam(Long)}
   */
  @Override
  public boolean evictTeam(Long teamId) {
    log.info("evictTeam(" + teamId + ")");
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
    log.info("setTeamStatus(" + teamId + ", " + status + ")");
    Team team = teamRepository.findById(teamId).orElse(null);
    if (team == null || team.getStatus() == status)
      return false;
    team.setStatus(status); // no need to save, will be flushed automatically at the end of transaction (since not a new entity)
    return true;
  }
}
