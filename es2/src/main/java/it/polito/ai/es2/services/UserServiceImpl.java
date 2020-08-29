package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.ProfessorDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.ProfessorRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.services.exceptions.FailedAddException;
import it.polito.ai.es2.services.exceptions.NullParameterException;
import it.polito.ai.es2.services.interfaces.UserService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserServiceImpl implements UserService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  ProfessorRepository professorRepository;

  @Override
  public boolean addProfessor(ProfessorDTO professor) {
//    log.info("addProfessor(" + professor + ")");
//    if (professor == null || professor.getId() == null) return false;
//    Professor p = modelMapper.map(professor, Professor.class);
//    try {
//      if (!professorRepository.existsById(professor.getId())) {
//        professorRepository.save(p);
//        return true;
//      }
//      return false;
//    } catch (IllegalArgumentException e) {
//      log.warning("###### IllegalArgumentException:" + e);
//      e.printStackTrace();
//      return false;
//    } catch (Exception e) {
//      log.warning("###### Other Exception:" + e);
//      e.printStackTrace();
    return false;
//    }
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
   * POST {@link it.polito.ai.es2.controllers.APIStudents_RestController#addStudent(StudentDTO)}
   */
  @Override
  public void addStudent(StudentDTO student) {
    log.info("addStudent(" + student + ")");
    if (student == null || student.getId() == null)
      throw new FailedAddException("null parameters");
    Student s = modelMapper.map(student, Student.class);
    if (studentRepository.existsById(student.getId())) {
      throw new FailedAddException("duplicate");
    }
    studentRepository.save(s);
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
