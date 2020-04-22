package it.polito.ai.es2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
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
import it.polito.ai.es2.services.exceptions.*;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
@Log
public class TeamServiceImpl implements TeamService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  CourseRepository cr;
  @Autowired
  StudentRepository sr;
  @Autowired
  TeamRepository tr;
  
  @Override
  public boolean addStudent(StudentDTO student) {
    if (student == null || student.getId() == null)
      return false;
    Student s = modelMapper.map(student, Student.class);
    try {
      if (!sr.existsById(student.getId())) {
        sr.save(s);
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
  
  @Override
  public boolean addCourse(CourseDTO course) {
    if (course == null || course.getName() == null) {
      return false;
    }
    Course c = modelMapper.map(course, Course.class);
    try {
      if (!cr.existsById(course.getName())) {
        cr.save(c);
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
  
  @Override
  public List<Boolean> addAll(List<StudentDTO> students) {
    if (students == null) {
      throw new TeamServiceException("null parameters");
    }
    return students.stream()
               .map(x -> modelMapper.map(x, Student.class))
               .peek(e -> log.info("addAll(List<StudentDTO> - size:" + students.size() + " - Entità corrente:" + e))
               .map(
                   e -> {
                     boolean b = sr.existsById(e.getId());
                     // Se studente esisteva già...
                     if (b)
                       return Boolean.FALSE;
                     // Se invece non esisteva...
                     sr.save(e);
                     return Boolean.TRUE;
                   }).collect(Collectors.toList());
  }
  
  @Override
  public Optional<CourseDTO> getCourse(String name) {
    if (name == null)
      throw new TeamServiceException("getCourse() - null parameter");
    return cr.findById(name).map(x -> modelMapper.map(x, CourseDTO.class));
  }
  
  @Override
  public Optional<StudentDTO> getStudent(String studentId) {
    if (studentId == null)
      throw new TeamServiceException("getStudent() - null parameter");
    return sr.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
  }
  
  @Override
  public List<CourseDTO> getAllCourses() {
    return cr.findAll().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<CourseDTO> getCourses(String studentId) {
    if (studentId == null) {
      throw new TeamServiceException("null parameters");
    }
    return sr.getOne(studentId).getCourses().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<StudentDTO> getAllStudents() {
    return sr.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public void enableCourse(String courseName) throws CourseNotFoundException {
    if (courseName == null)
      throw new CourseNotFoundException("null parameter");
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("course not found");
    cr.getOne(courseName).setEnabled(true);
  }
  
  @Override
  public void disableCourse(String courseName) throws CourseNotFoundException {
    if (courseName == null)
      throw new CourseNotFoundException("null parameter");
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("course not found");
    cr.getOne(courseName).setEnabled(false);
  }
  
  @Override
  public boolean addStudentToCourse(String studentId, String courseName) throws StudentNotFoundException, CourseNotFoundException {
//    log.info("##### loading addStudentToCourse(" + studentId + "," + courseName + ")" + " - Initial checks + repos.getOne####");
    if (studentId == null || courseName == null)
      throw new TeamServiceException("addStudentToCourse() - null parameters");
    if (!sr.existsById(studentId))
      throw new StudentNotFoundException("addStudentToCourse() - student not found:" + studentId);
    Optional<Course> courseOptional = cr.findById(courseName);
    if (!courseOptional.isPresent())
      throw new CourseNotFoundException("addStudentToCourse() - course not found:" + courseName);
    Course c = courseOptional.get();
    // Controllo che corso sia enabled
    if (!c.isEnabled())
      return false;
    Student s = sr.getOne(studentId);
    // Controllo che studente non sia già iscritto al corso
    if (c.getStudents().stream().anyMatch(x -> x.getId().equals(studentId)))
      return false;
    c.addStudent(s);
    log.info("addStudentToCourse(" + studentId + "," + courseName + "). " + c.getName() + "->ListStudents: " + c.getStudents());
    return true;
  }
  
  @Override
  public List<Boolean> enrollAll(List<String> studentIds, String courseName) {
    if (studentIds == null || courseName == null) {
      throw new TeamServiceException("enrollAll(List<String> studentIds, String courseName) - null parameters");
    }
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("enrollAll(List<String> studentIds, String courseName) - course not found");
    Course course = cr.getOne(courseName);
    List<Boolean> lb = new ArrayList<>();
    for (String id : studentIds) {
      if (id == null) { // null was put by AddAndReroll function
        lb.add(false);
        continue;
      }
      if (!sr.existsById(id))
        throw new StudentNotFoundException("enrollAll(List<String> studentIds, String courseName) - student in list not found");
      Student student = sr.getOne(id);
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
  
  @Override
  public List<StudentDTO> getEnrolledStudents(String courseName) throws CourseNotFoundException {
    if (courseName == null)
      throw new TeamServiceException("getEnrolledStudents() - null parameters");
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("getEnrolledStudents() - StudentNotFoundException: (" + courseName + ")");
    Course c = cr.getOne(courseName);
    return c.getStudents().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  /**
   * Prendo il CSV dal controller (implementato)
   */
  @Override
  public List<Boolean> addAndEroll(Reader reader, String courseName) {
    if (reader == null || courseName == null) {
      throw new TeamServiceException("addAndEroll(Reader reader, String courseName) - null parameters");
    }
    Optional<Course> courseOptional = cr.findById(courseName);
    if (!courseOptional.isPresent())
      throw new CourseNotFoundException("addAndEroll(Reader reader, String courseName) - course not found");
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
                                  Optional<Student> optionalStudent_fromDb = sr.findById(new_studentDTO.getId());
                                  if (optionalStudent_fromDb.isPresent())
                                    return null;
                                  Student newStudent = modelMapper.map(new_studentDTO, Student.class);
                                  return sr.save(newStudent);
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
  
  @Override
  public List<TeamDTO> getTeamsForStudent(String studentId) {
    if (studentId == null)
      throw new TeamServiceException("getTeamsForStudent() - null parameters");
    return sr.getOne(studentId).getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<StudentDTO> getMembers(Long teamId) {
    if (teamId == null)
      throw new TeamServiceException("getMembers() - null parameters");
    Optional<Team> team = tr.findById(teamId);
    if (team.isPresent())
      return team.get().getMembers().stream().filter(Objects::nonNull).map(y -> modelMapper.map(y, StudentDTO.class)).collect(Collectors.toList());
    else
      throw new TeamServiceException("getMembers() - team not found");
  }
  
  /**
   * Si dà per assunto che il team non sia stato già creato (non si controlla se name già presente, solo id)
   */
  @Override
  public TeamDTO proposeTeam(String courseId, String name, List<String> memberIds) throws TeamServiceException {
    if (courseId == null || name == null || memberIds == null)
      throw new TeamServiceException("null parameter");
    Optional<Course> oc = cr.findById(courseId);
    if (!oc.isPresent())
      throw new CourseNotFoundException("proposeTeam - course not found");
    if (!oc.get().isEnabled())
      throw new CourseNotEnabledException("proposeTeam() - course not enabled");
    List<Optional<Student>> streamopt = memberIds.stream().map(x -> sr.findById(x)).collect(Collectors.toList());
    if (!streamopt.stream().allMatch(Optional::isPresent))
      throw new StudentNotFoundException("proposeTeam() - student not found");
    
    Course course = oc.get();
    List<Student> listMemberStudents = streamopt.stream().map(Optional::get).collect(Collectors.toList());
    // !listMemberStudents.stream().allMatch(x -> course.getStudents().contains(x))
    if (!course.getStudents().containsAll(listMemberStudents))
      throw new StudentNotEnrolledException("proposeTeam() - non tutti gli studenti sono iscritti al corso");
    
    if (course.getTeams().size() != 0 && course.getTeams().stream().map(Team::getMembers).noneMatch(y -> {
      for (Student student : y) {
        if (listMemberStudents.stream().anyMatch(student::equals))
          return true;
      }
      return false;
    }))
      throw new StudentInMultipleTeamsException("proposeTeam() - studenti fanno parte di altri gruppi nell’ambito dello stesso corso");
    if (listMemberStudents.size() < course.getMin() || listMemberStudents.size() > course.getMax())
      throw new CourseCardinalConstrainsException("proposeTeam() - non rispettati i vincoli di cardinalità definiti nell’ambito del corso");
    if (!listMemberStudents.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException("proposeTeam() - duplicati nell'elenco dei partecipanti");
    
    TeamDTO teamDTO = new TeamDTO(null, name, 0);
    Team team = modelMapper.map(teamDTO, Team.class);
    for (Student student : new ArrayList<>(listMemberStudents)) {
      student.addTeam(team); // add su studenti
    }
    course.addTeam(team); // add su corsi
    
    // Se team era già presente (stesso nome), lo aggiorno, cancellando prima il vecchio e poi inserendo il nuovo
    for (Team others_team_in_course : course.getTeams()) {
      if (others_team_in_course.getId() != null && others_team_in_course.getName().equals(team.getName())) {
        tr.deleteById(others_team_in_course.getId());
        tr.flush();
      }
    }
    team = tr.save(team);
    return teamDTO;
  }
  
  @Override
  public List<TeamDTO> getTeamForCourse(String courseName) {
    if (courseName == null)
      throw new TeamServiceException("null parameter");
    Optional<Course> co = cr.findById(courseName);
    if (!co.isPresent())
      throw new CourseNotFoundException("getTeamForCourse - course not found");
    return co.get().getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<StudentDTO> getStudentsInTeams(String courseName) {
    if (courseName == null)
      throw new TeamServiceException("null parameter");
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("getTeamForCourse - course not found");
    return cr.getStudentsInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<StudentDTO> getAvailableStudents(String courseName) {
    if (courseName == null)
      throw new TeamServiceException("null parameter");
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("getTeamForCourse - course not found");
    return cr.getStudentsNotInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
}
