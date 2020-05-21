package it.polito.ai.es2.services;

import it.polito.ai.es2.entities.Role;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.CourseRepository;
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
  CourseRepository courseRepository;
  @Autowired
  TeamRepository teamRepositoryea;
  
  public boolean isTeamOwner(Long id, String principal_username) {
    List<Student> students = teamRepositoryea.findById(id).map(team -> team.getMembers()).orElse(null);
    if (students == null)
      return false;
    return students.stream().anyMatch(student -> student.getId().equals(principal_username));
  }
  
  public boolean isOwner(String parameter, String principal_username) {
    return parameter.equals(principal_username);
  }
  
  // TODO: delete all the rest
  
  public boolean hasPermissions(String username, User u) {
    // if user from @AuthenticationPrincipal id is equal to userId from @PathVariable
    log.info("hasPermissions with @AuthPrincipal user id:" + username + " - Corso from @PathVariable:" + "corso");

/*        for (Role role : user.getRoles()) {
            if (role.getName().equalsIgnoreCase("ROLE_ADMIN")) {
                log.info("detected admin permissions (ROLE_ADMIN)");
                return true;
            }
        }*/
/*      Course course = courseRepository.findById(corso).orElse(null);
      if (course != null && course.getProfessor() != null)
          return course.getProfessor().equals(username);
      return false;*/
    return courseRepository.findById("corso").map(x -> x.getProfessor()).map(y -> y.equals(username)).orElse(false);
  }
  
  public boolean hasPermissions(String username) {
    // if user from @AuthenticationPrincipal id is equal to userId from @PathVariable
    log.info("####hasPermissions with @AuthPrincipal:" + username);
    
    return true;
  }
  
  public boolean hasAdminPermissions(User user) {
    for (Role role : user.getRoles()) {
      if (role.getName().equalsIgnoreCase("ROLE_ADMIN")) {
        log.info("detected admin permissions (ROLE_ADMIN)");
        return true;
      }
    }
    
    return false;
  }
}
