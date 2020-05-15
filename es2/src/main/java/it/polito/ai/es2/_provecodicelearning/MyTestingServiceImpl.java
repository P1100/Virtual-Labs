package it.polito.ai.es2._provecodicelearning;

import it.polito.ai.es2.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyTestingServiceImpl implements MyTestingService {
  @Autowired
  CourseRepository cr;
  
  @Override
  public void entity_manager_test() {
    cr.advancedSearchCourses();
//    System.out.println(cr.advancedSearchCourses());
  }
}
