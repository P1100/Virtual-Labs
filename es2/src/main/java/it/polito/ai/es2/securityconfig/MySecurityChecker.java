package it.polito.ai.es2.securityconfig;

import it.polito.ai.es2.entities.Professor;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Log
public class MySecurityChecker {
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  TeamRepository teamRepository;

  public boolean isCourseOwner(String course, String principal_username) {
    if (course.isBlank() || principal_username.isBlank())
      return false;
    List<Professor> teachers = courseRepository.findById(course).map(x -> x.getProfessors()).orElse(new ArrayList<Professor>());
    return teachers.stream().anyMatch(x -> principal_username.equals(x.getId()));
  }

  public boolean isTeamOwner(Long id, String principal_username) {
    if (id == null || principal_username.isBlank())
      return false;
    List<Student> students = teamRepository.findById(id).map(team -> team.getMembers()).orElse(null);
    if (students == null)
      return false;
    return students.stream().anyMatch(student -> student.getId().equals(principal_username));
  }

  public boolean isOwner(String parameter, String principal_username) {
    if (parameter.isBlank() || principal_username.isBlank())
      return false;
    return parameter.equals(principal_username);
  }
}
