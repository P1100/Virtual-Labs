package it.polito.ai.es2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.es2.controllers.APICourses_RestController;
import it.polito.ai.es2.domains.StudentViewModel;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Professor;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.services.exceptions.*;
import it.polito.ai.es2.services.interfaces.CourseService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class CourseServiceImpl implements CourseService {
  private final static boolean adminRoleAddAll = true;
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  StudentRepository studentRepository;

  /**
   * GET {@link APICourses_RestController#getAllCourses()}
   */
  @Override
  @PreAuthorize("isAuthenticated()") // overrides class roles
  public List<CourseDTO> getAllCourses() {
    log.info("getAllCourses");
    return courseRepository.findAll().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getCourse(String)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public Optional<CourseDTO> getCourse(String courseId) {
    log.info("getCourse(" + courseId + ")");
    if (courseId == null) return Optional.empty();
    return courseRepository.findById(courseId).map(x -> modelMapper.map(x, CourseDTO.class));
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getEnrolledStudents(String)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public List<StudentDTO> getEnrolledStudents(String courseId) {
    log.info("getEnrolledStudents(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException("null parameter");
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty())
      throw new CourseNotFoundException(courseId);
    return courseOptional.get().getStudents().stream()
        .map(st -> {
          st.setTeamName(st.getTeams().stream()
              .filter(t -> t.getCourse().getId().equals(courseId))
              .findAny().map(t -> t.getName() + "found").orElse("not")); // TODO: clean later
          return st;
        })
        .map(x -> modelMapper.map(x, StudentDTO.class))
        .collect(Collectors.toList());
  }

  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enableCourse(String)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void enableCourse(String courseId) {
    log.info("enableCourse(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException();
    Optional<Course> optionalCourse = courseRepository.findById(courseId);
    if (optionalCourse.isEmpty()) throw new CourseNotFoundException(courseId);
    optionalCourse.get().setEnabled(true);
  }

  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#disableCourse(String)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void disableCourse(String courseId) {
    log.info("disableCourse(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException();
    Optional<Course> optionalCourse = courseRepository.findById(courseId);
    if (optionalCourse.isEmpty()) throw new CourseNotFoundException(courseId);
    optionalCourse.get().setEnabled(false);
  }

  /**
   * POST {@link it.polito.ai.es2.controllers.APICourses_RestController#addCourse(CourseDTO)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void addCourse(CourseDTO courseDTO) {
    log.info("addCourse(" + courseDTO + ")");
    if (courseDTO == null || courseDTO.getId() == null)
      throw new FailedAddException("null parameters");
    if (courseRepository.existsById(courseDTO.getId()))
      throw new FailedAddException("duplicate");
    courseRepository.save(modelMapper.map(courseDTO, Course.class));
  }

  /**
   * PUT {@link it.polito.ai.es2.controllers.APICourses_RestController#updateCourse(CourseDTO)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void updateCourse(CourseDTO courseDTO) {
    log.info("updateCourse(" + courseDTO + ")");
    if (courseDTO == null || courseDTO.getId() == null)
      throw new FailedUpdateException("null parameters");
    if (!courseRepository.existsById(courseDTO.getId()))
      throw new FailedUpdateException("not found");
    int min = courseDTO.getMinSizeTeam();
    int max = courseDTO.getMaxSizeTeam();
    if (max < min)
      throw new CourseCardinalityConstrainsException(courseDTO.getId(), min + " < " + max);
    if (courseRepository.countTeamsThatViolateCardinality(courseDTO.getId(), min, max) != 0)
      throw new CourseCardinalityConstrainsException(courseDTO.getId(), "new cardinalities incompatible with existing teams");
    Course old = courseRepository.findById(courseDTO.getId()).orElse(null);
    Course neww = modelMapper.map(courseDTO, Course.class);
    /* NEEDED! Because owning side overwrites join table */
    neww.setStudents(old.getStudents());
    neww.setProfessors(old.getProfessors());
    courseRepository.save(neww);
  }

  /**
   * DELETE {@link it.polito.ai.es2.controllers.APICourses_RestController#deleteCourse(String)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void deleteCourse(String courseId) {
    log.info("deleteCourse(" + courseId + ")");
    if (courseId == null)
      throw new CourseNotFoundException();
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty())
      throw new CourseNotFoundException(courseId);
    Course c = courseOptional.get();
    // removing course from synced entities before delete
    for (Student student : c.getStudents()) {
      student.getCourses().remove(c);
    }
    c.setStudents(new ArrayList<>());
    for (Professor professor : c.getProfessors()) {
      professor.getCourses().remove(c);
    }
    c.setProfessors(new ArrayList<>());
    // ----> Others sync handled by delete cascade!
    courseRepository.deleteById(courseId);
  }

  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#disenrollStudent(Long, String)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void disenrollStudent(Long studentId, String courseId) {
    log.info("disenrollStudent(" + studentId + ", " + courseId + ")");
    if (studentId == null || courseId == null) throw new NullParameterException("student id, course id");
    Optional<Student> studentOptional = studentRepository.findById(studentId);
    if (studentOptional.isEmpty()) throw new StudentNotFoundException(studentId.toString());
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty()) throw new CourseNotFoundException(courseId);
    courseOptional.get().removeStudent(studentOptional.get());
  }

  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudent(String, Map)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public void enrollStudent(Long studentId, String courseId) {
    log.info("enrollStudent(" + studentId + ", " + courseId + ")");
    if (studentId == null || courseId == null) throw new NullParameterException("student id, course id");
    Optional<Student> studentOptional = studentRepository.findById(studentId);
    if (studentOptional.isEmpty()) throw new StudentNotFoundException(studentId.toString());
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty()) throw new CourseNotFoundException(courseId);
    Course c = courseOptional.get();
    if (!c.isEnabled())
      throw new CourseNotEnabledException(courseId);
    if (c.getStudents().stream().anyMatch(x -> x.getId().equals(studentId)))
      throw new StudentAlreadyEnrolled(studentId.toString(), courseId);
    c.addStudent(studentOptional.get());
  }

  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudents(List, String)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')") // and @mySecurityChecker.isCourseOwner(#courseName,authentication.principal.username))")
  public List<Boolean> enrollStudents(List<Long> studentIds, String courseId) {
    log.info("enrollStudents(" + studentIds + ", " + courseId + ")");
    if (studentIds == null || courseId == null)
      throw new NullParameterException("students id list, course id");
    if (!courseRepository.existsById(courseId))
      throw new CourseNotFoundException(courseId);

    Course course = courseRepository.getOne(courseId);
    if (!course.isEnabled())
      throw new CourseNotEnabledException(courseId);
    List<Boolean> lb = new ArrayList<>();
    for (Long id : studentIds) {
      if (id == null) { // null was put by AddAndReroll function
        lb.add(false);
        continue;
      }
      if (!studentRepository.existsById(id))
        throw new StudentNotFoundException(id.toString());
      Student student = studentRepository.getOne(id);
      // Controllo che lo studente corrente (id) non sià già presente nella lista degli studenti iscritti al corso
      if (course.getStudents().stream().anyMatch(x -> x.getId().equals(id))) {
        lb.add(false);
        continue;
      }
      lb.add(true);
      course.addStudent(student);
    }
    log.info("enrollStudents returned List<Boolean>:" + lb);
    return lb;
  }

  /**
   * {@link it.polito.ai.es2.controllers.APICourses_RestController#enrollStudentsCSV(String, MultipartFile)}
   */
  @Override
  @PreAuthorize("hasRole('PROFESSOR')")
  public List<Boolean> enrollStudentsCSV(Reader reader, String courseId) {
    log.info("enrollStudentsCSV(" + reader + ", " + courseId + ")");
    if (reader == null || courseId == null) throw new NullParameterException("reader, course id");
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty()) throw new CourseNotFoundException(courseId);
    if (!courseOptional.get().isEnabled())
      throw new CourseNotEnabledException(courseId);

    CsvToBean<StudentViewModel> csvToBean = new CsvToBeanBuilder<StudentViewModel>(reader)
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
                                Student newStudent = modelMapper.map(new_studentDTO, Student.class);
//                                TODO: enable only if admin! Possibilities:
//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); user.getAuthorities();
//                                httpServletRequest.isUserInRole("ADMIN");
// !!!!!                          printWelcome(ModelMap model, Authentication authentication)
//                                printWelcome(ModelMap model, Pricipal principal)
                                if (optionalStudent_fromDb.isEmpty()) {
                                  if (!adminRoleAddAll)
                                    return null;
                                }
                                return studentRepository.save(newStudent);
                              })
                              .map(y -> y != null ? y.getId() : null).collect(Collectors.toList());
    log.info(courseId + " - enrollStudentsCSV returned Valid Students: " + list_ids);
    return enrollStudents(list_ids, courseId);
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getTeamsForCourse(String)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public List<TeamDTO> getTeamsForCourse(String courseId) {
    log.info("getTeamsForCourse(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException("[null]");
    Optional<Course> co = courseRepository.findById(courseId);
    if (co.isEmpty()) throw new CourseNotFoundException(courseId);
    return co.get().getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getEnrolledWithTeam(String)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public List<StudentDTO> getEnrolledWithTeam(String courseId) {
    log.info("getStudentsInTeams(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException("[null]");
    if (!courseRepository.existsById(courseId)) throw new CourseNotFoundException(courseId);
    return courseRepository.getStudentsInTeams(courseId).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APICourses_RestController#getEnrolledWithoutTeam(String)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public List<StudentDTO> getEnrolledWithoutTeam(String courseId) {
    log.info("getAvailableStudents(" + courseId + ")");
    if (courseId == null) throw new CourseNotFoundException("[null]");
    if (!courseRepository.existsById(courseId)) throw new CourseNotFoundException(courseId);
    return courseRepository.getStudentsNotInTeams(courseId).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
}
