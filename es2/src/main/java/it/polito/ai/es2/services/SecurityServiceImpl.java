package it.polito.ai.es2.services;

import it.polito.ai.es2.entities.Role;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.CourseRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log
public class SecurityServiceImpl implements SecurityService {
  @Autowired
  CourseRepository courseRepository;
  
  @Override
  public boolean hasPermissions(String username, String corso) {
    // if user from @AuthenticationPrincipal id is equal to userId from @PathVariable
    log.info("hasPermissions with @AuthPrincipal user id:" + username + " - Corso from @PathVariable:" + corso);

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
    return courseRepository.findById(corso).map(x -> x.getProfessor()).map(y -> y.equals(username)).orElse(false);
  }
  
  @Override
  public boolean hasPermissions(String username) {
    // if user from @AuthenticationPrincipal id is equal to userId from @PathVariable
    log.info("####hasPermissions with @AuthPrincipal:" + username);
    
    return true;
  }
  
  @Override
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
