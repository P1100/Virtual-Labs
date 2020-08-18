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
  NotificationService notificationService;
  @Autowired
  AssignmentRepository assignmentRepository;
  @Autowired
  ImageRepository imageRepository;
  @Autowired
  ImplementationRepository implementationRepository;
  @Autowired
  VMRepository vmRepository;
  
  @Override
  public void testing(
  ) {
//    Image test = new Image();
//    test.setRevisionCycle(0);
//    imageRepository.saveAndFlush(test);
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
  public Optional<CourseDTO> getCourse(String courseId) {
    log.info("getCourse(" + courseId + ")");
    if (courseId == null) return Optional.empty();
    return courseRepository.findById(courseId).map(x -> modelMapper.map(x, CourseDTO.class));
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enableCourse(String)}
   */
  @Override
  public void enableCourse(String courseId) {
    log.info("enableCourse(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException("null parameter");
    Optional<Course> optionalCourse = courseRepository.findById(courseId);
    if (optionalCourse.isEmpty()) throw new CourseNotFoundException("Course not found! " + courseId);
    optionalCourse.get().setEnabled(true);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#disableCourse(String)}
   */
  @Override
  public void disableCourse(String courseId) {
    log.info("disableCourse(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException("null parameter");
    Optional<Course> optionalCourse = courseRepository.findById(courseId);
    if (optionalCourse.isEmpty()) throw new CourseNotFoundException("Course not found! " + courseId);
    optionalCourse.get().setEnabled(false);
  }
  
  /**
   * POST {@link it.polito.ai.es2.controllers.APICourses_RestController#addCourse(CourseDTO)}
   */
  @Override
  public boolean addCourse(CourseDTO courseDTO) {
    log.info("addCourse(" + courseDTO + ")");
    if (courseDTO == null || courseDTO.getId() == null) return false;
    Course c = modelMapper.map(courseDTO, Course.class);
    if (!courseRepository.existsById(courseDTO.getId())) {
      courseRepository.save(c);
      return true;
    }
    return false;
  }
  @Override
  public boolean updateCourse(CourseDTO courseDTO) {
    log.info("updateCourse(" + courseDTO + ")");
    if (courseDTO == null || courseDTO.getId() == null) return false;
    Course c = modelMapper.map(courseDTO, Course.class);
    courseRepository.save(c);
    return true;
  }
  
  @Override
  public boolean deleteCourse(String courseId) {
    log.info("deleteCourse(" + courseId + ")");
    if (courseId == null) return false;
    courseRepository.deleteById(courseId);
    return true;
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getEnrolledStudents(String)}
   */
  @Override
  public List<StudentDTO> getEnrolledStudents(String courseId) throws CourseNotFoundException {
    log.info("getEnrolledStudents(" + courseId + ")");
    if (courseId == null) throw new TeamServiceException("null course parameter");
    if (!courseRepository.existsById(courseId))
      throw new CourseNotFoundException("Course not found! " + courseId);
    Course c = courseRepository.getOne(courseId);
    return c.getStudents().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getStudentsInTeams(String)}
   */
  @Override
  public List<StudentDTO> getStudentsInTeams(String courseId) {
    log.info("getStudentsInTeams(" + courseId + ")");
    if (courseId == null) throw new TeamServiceException("null course parameter");
    if (!courseRepository.existsById(courseId)) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return courseRepository.getStudentsInTeams(courseId).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getAvailableStudents(String)}
   */
  @Override
  public List<StudentDTO> getAvailableStudents(String courseId) {
    log.info("getAvailableStudents(" + courseId + ")");
    if (courseId == null) throw new TeamServiceException("null course parameter");
    if (!courseRepository.existsById(courseId)) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return courseRepository.getStudentsNotInTeams(courseId).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getTeamsForCourse(String)}
   */
  @Override
  public List<TeamDTO> getTeamsForCourse(String courseId) {
    log.info("getTeamsForCourse(" + courseId + ")");
    if (courseId == null) throw new TeamServiceException("null course parameter");
    Optional<Course> co = courseRepository.findById(courseId);
    if (co.isEmpty()) throw new CourseNotFoundException("getTeamForCourse - course not found");
    return co.get().getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#disenrollStudent(Long, String)}
   */
  @Override
  public void disenrollStudent(Long studentId, String courseId) {
    log.info("disenrollStudent(" + studentId + ", " + courseId + ")");
    if (studentId == null || courseId == null) throw new TeamServiceException("null student or course parameter");
    if (!studentRepository.existsById(studentId)) throw new StudentNotFoundException("disenrollStudent() - student not found:" + studentId);
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty()) throw new CourseNotFoundException("disenrollStudent() - course not found:" + courseId);
    
    Course c = courseOptional.get();
    Student s = studentRepository.getOne(studentId);
    c.removeStudent(s);
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudent(String, Map)}
   */
  @Override
  public boolean enrollStudent(Long studentId, String courseId) {
    log.info("disenrollStudent(" + studentId + ", " + courseId + ")");
    if (studentId == null || courseId == null) throw new TeamServiceException("null student or course parameter");
    if (!studentRepository.existsById(studentId)) throw new StudentNotFoundException("enrollStudent() - student not found:" + studentId);
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty()) throw new CourseNotFoundException("enrollStudent() - course not found:" + courseId);
    
    Course c = courseOptional.get();
    // Controllo che corso sia enabled
    if (!c.isEnabled())
      return false;
    Student s = studentRepository.getOne(studentId);
    // Controllo che studente non sia già iscritto al corso
    if (c.getStudents().stream().anyMatch(x -> x.getId().equals(studentId)))
      return false;
    
    c.addStudent(s);
    return true;
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudents(List, String)}
   */
  @Override
  public List<Boolean> enrollStudents(List<Long> studentIds, String courseId) {
    log.info("enrollStudents(" + studentIds + ", " + courseId + ")");
    if (studentIds == null || courseId == null)
      throw new TeamServiceException("null list of students or course parameter");
    if (!courseRepository.existsById(courseId))
      throw new CourseNotFoundException("enrollStudents(List<Long> studentIds, String courseId) - course not found");
    
    Course course = courseRepository.getOne(courseId);
    List<Boolean> lb = new ArrayList<>();
    for (Long id : studentIds) {
      if (id == null) { // null was put by AddAndReroll function
        lb.add(false);
        continue;
      }
      if (!studentRepository.existsById(id))
        throw new StudentNotFoundException("enrollStudents(List<Long> studentIds, String courseId) - student in list not found");
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
    log.info("enrollStudents List<Boolean> ritornata:" + lb);
    return lb;
  }
  
  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudentsCSV(String, MultipartFile)}
   */
  @Override
  public List<Boolean> enrollStudentsCSV(Reader reader, String courseName) {
    log.info("addAndEroll(" + reader + ", " + courseName + ")");
    if (reader == null || courseName == null) throw new TeamServiceException("null reader or course parameter");
    Optional<Course> courseOptional = courseRepository.findById(courseName);
    if (courseOptional.isEmpty()) throw new CourseNotFoundException("addAndEroll(Reader reader, String courseName) - course not found");
    
    CsvToBean<StudentViewModel> csvToBean = new CsvToBeanBuilder(reader)
                                                .withType(StudentViewModel.class)
                                                .withIgnoreLeadingWhiteSpace(true)
                                                .build();
    List<StudentViewModel> users = csvToBean.parse();
    log.info(String.valueOf(users));
    List<Long> list_ids = users.stream()
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
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getAllStudents()}
   */
  @Override
  public List<StudentDTO> getAllStudents() {
    log.info("getAllStudents()");
    return studentRepository.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getStudent(Long)}
   */
  @Override
  public Optional<StudentDTO> getStudent(Long studentId) {
    log.info("getStudent(" + studentId + ")");
    if (studentId == null) throw new TeamServiceException("null student parameter");
    return studentRepository.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getCourses(Long)}
   */
  @Override
  public List<CourseDTO> getCourses(Long studentId) {
    log.info("getCourses(" + studentId + ")");
    if (studentId == null) throw new TeamServiceException("null student parameter");
    return studentRepository.getOne(studentId).getCourses().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APIStudent_RestController#getTeamsForStudent(Long)}
   */
  @Override
  public List<TeamDTO> getTeamsForStudent(Long studentId) {
    log.info("getTeamsForStudent(" + studentId + ")");
    if (studentId == null) throw new TeamServiceException("null student parameter");
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
    if (teamId == null) throw new TeamServiceException("null team parameter");
    return teamRepository.findById(teamId).map(x -> modelMapper.map(x, TeamDTO.class));
  }
  
  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
   */
  @Override
  public List<StudentDTO> getMembers(Long teamId) {
    log.info("getMembers(" + teamId + ")");
    if (teamId == null) throw new TeamServiceException("null team parameter");
    Optional<Team> team = teamRepository.findById(teamId);
    if (team.isPresent())
      return team.get().getMembers().stream().filter(Objects::nonNull).map(y -> modelMapper.map(y, StudentDTO.class)).collect(Collectors.toList());
    else
      throw new TeamNotFoundException("getMembers() - team not found");
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
   * POST {@link it.polito.ai.es2.controllers.APIStudent_RestController#addAll(List)}
   */
  @Override
  public List<Boolean> addAllStudents(List<StudentDTO> students) {
    log.info("addAll(" + students + ")");
    if (students == null) throw new TeamServiceException("null list of students parameter");
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
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#proposeTeam(String, String, List)}
   */
  // TODO: credo di aver fatto in modo che non ci possono essere piú gruppi con lo stesso nome per lo stesso corso, check in fase di creazione
  @Override
  public TeamDTO proposeTeam(String courseName, String team_name, List<Long> memberIds) {
    log.info("proposeTeam(" + courseName + ", " + team_name + ", " + memberIds + ")");
    if (courseName == null || team_name == null || memberIds == null)
      throw new TeamServiceException("null student or course or list of memberIds parameter");
    Optional<Course> oc = courseRepository.findById(courseName);
    if (oc.isEmpty()) throw new CourseNotFoundException("proposeTeam - course not found");
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
            course.getTeams()
                .stream()
                .map(Team::getMembers)
                .flatMap(List::stream)
                .distinct()
                .anyMatch(student -> listStudentsProposal.stream().anyMatch(student::equals))
    )
      throw new StudentInMultipleTeamsException("proposeTeam() - studenti fanno parte di altri gruppi nell’ambito dello stesso corso");
    if (listStudentsProposal.size() < course.getMinEnrolled() || listStudentsProposal.size() > course.getMaxEnrolled())
      throw new CourseCardinalConstrainsException("proposeTeam() - non rispettati i vincoli di cardinalità del corso su dimensioni team");
    if (!listStudentsProposal.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException("proposeTeam() - duplicati nell'elenco dei partecipanti della proposta team");
    if (teamRepository.findFirstByNameAndCourse_id(team_name, courseName) != null)
      throw new TeamAlreayCreatedException("proposeTeam() - team già creato");
    
    TeamDTO teamDTO = new TeamDTO();
    teamDTO.setName(team_name);
    teamDTO.setStatus(Team.status_inactive());
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
    if (optionalTeam.isEmpty())
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
