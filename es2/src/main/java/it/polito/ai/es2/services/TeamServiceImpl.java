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
import it.polito.ai.es2.services.exceptions.CourseNotFoundException;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamServiceException;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: Controlli su presenza valore nel repository, da 2 istruzione (repo.existsID+repo.getOne) a <Optional> repo.findById() !
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
    List<Boolean> lb = students.stream()
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
    return lb;
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
    log.info("enrollAll List<Boolean> ritornata (lb):" + lb);
    return lb;
  }
  
  @Override
  public List<StudentDTO> getEnrolledStudents(String courseName) throws CourseNotFoundException {
    if (courseName == null)
      throw new TeamServiceException("getEnrolledStudents() - null parameters");
    if (!cr.existsById(courseName))
      throw new CourseNotFoundException("getEnrolledStudents() - StudentNotFoundException: (" + courseName + ")");
    Course c = cr.getOne(courseName);
    List<StudentDTO> returnListDTO = c.getStudents().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
    return returnListDTO;
  }
  
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
                                    return optionalStudent_fromDb.get(); // return null; --> se uso filter sotto
                                  Student newStudent = modelMapper.map(new_studentDTO, Student.class);
                                  return sr.save(newStudent);
                                })
//                                // Non serve controllare che student non si già presente, lo faccio in enrollAll
//                                .filter(k -> {
//                                    if (k == null)
//                                      return false;
//                                    else
//                                      return true;
//                                  })
                                .map(y -> y.getId()).collect(Collectors.toList());
    log.info(courseName + " - CSV_Student_Ids: " + list_ids);
    return enrollAll(list_ids, courseName);
  }
  
  @Override
  public List<TeamDTO> getTeamsForStudent(String studentId) {
    return sr.getOne(studentId).getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<StudentDTO> getMembers(Long teamId) {
    return tr.getOne(teamId).getMembers().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public TeamDTO proposeTeam(String courseId, String name, List<String> memberIds) throws TeamServiceException {
    if (courseId == null || name == null || memberIds == null)
      throw new TeamServiceException("null parameter");
    // Si dà per assunto che il team non sia stato già creato
    Optional<Course> oc = cr.findById(courseId);
    if (!oc.isPresent())
      throw new CourseNotFoundException("courseId not found");
    if (oc.get().isEnabled() == false)
      throw new TeamServiceException("proposeTeam() - course not enabled");
    Course course = oc.get();
    List<Optional<Student>> streamopt = memberIds.stream().map(x -> sr.findById(x)).collect(Collectors.toList());
    if (!streamopt.stream().allMatch(x -> x.isPresent()))
      throw new StudentNotFoundException("courseId not found");
    List<Student> listMemberStudents = streamopt.stream().map(x -> x.get()).collect(Collectors.toList());
    if (!listMemberStudents.stream().allMatch(x -> course.getStudents().contains(x)))
      throw new TeamServiceException("proposeTeam() - non tutti gli studenti sono iscritti al corso");
    if (course.getTeams().size() != 0 && course.getTeams().stream().map(x -> x.getMembers()).noneMatch(y -> {
      for (Student student : y) {
        if (listMemberStudents.stream().anyMatch(student::equals))
          return true;
      }
      return false;
    }))
      throw new TeamServiceException("proposeTeam() - studenti fanno parte di altri gruppi nell’ambito dello stesso corso");
    if (listMemberStudents.size() < course.getMin() || listMemberStudents.size() > course.getMax())
      throw new TeamServiceException("proposeTeam() - non rispettati i vincoli di cardinalità definiti nell’ambito del corso");
    if (!listMemberStudents.stream().allMatch(new HashSet<>()::add))
      throw new TeamServiceException("proposeTeam() - duplicati nell'elenco dei partecipanti");
    TeamDTO teamDTO = new TeamDTO(name, 1);
    Team team = modelMapper.map(teamDTO, Team.class);
    for (Student x : listMemberStudents.stream().collect(Collectors.toList())) {
      team.addStudent(x); // add su studenti
    }
    course.addTeam(team); // add su corsi
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
    return cr.getStudentsInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<StudentDTO> getAvailableStudents(String courseName) {
    return cr.getStudentsNotInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
  }
}
