package it.polito.ai.es2._provecodicelearning;

import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.entities.JwtRole;
import it.polito.ai.es2.entities.JwtUser;
import it.polito.ai.es2.repositories.JwtUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyTestingServiceImpl implements MyTestingService {
  @Autowired
  CourseRepository cr;
  @Autowired
  JwtUserRepository jwtUserRepository;
  @Autowired
  private PasswordEncoder bcryptEncoder;
  
  @Override
  public void entity_manager_test() {
    cr.advancedSearchCourses();
//    System.out.println(cr.advancedSearchCourses());
  }
  @Override
  public boolean addUser(String user, String pass, JwtRole role) {
//    User u = new User("admin2", bcryptEncoder.encode("adminpassword"), Role.ADMIN);
    if (jwtUserRepository.findTopByUsername(user)==null) {
      JwtUser u = new JwtUser(user, bcryptEncoder.encode(pass),role);
      jwtUserRepository.save(u);
      return true;
    }
    return false;
  }
  
  @Override
  public boolean checkUser(String user, String pass) {
    JwtUser u = jwtUserRepository.findTopByUsername(user);
    return bcryptEncoder.matches(pass, u.getPassword());
  }
}
