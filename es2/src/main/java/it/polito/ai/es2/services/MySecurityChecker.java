package it.polito.ai.es2.services;

import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    String professor_course = courseRepository.findById(course).map(x -> x.getProfessor()).orElse("");
    return principal_username.equals(professor_course);
  }
  
  public boolean isTeamOwner(Long id, String principal_username) {
    List<Student> students = teamRepository.findById(id).map(team -> team.getMembers()).orElse(null);
    if (students == null)
      return false;
    return students.stream().anyMatch(student -> student.getId().equals(principal_username));
  }
  
  public boolean isOwner(String parameter, String principal_username) {
    return parameter.equals(principal_username);
  }
}
