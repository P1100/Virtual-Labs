package it.polito.ai.es2.services;

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
  
    /* --> NOT WORKING (????), class not interface
  @Repository
  public class EmployeeDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
// not working?
    public List<?> getListOfStates(int userId) {
      
      String hql = "FROM states s WHERE " + "s.userid = :userId";
      Session session = entityManager.unwrap(Session.class);
      Query query = (Query) session.createQuery(hql);
      query..setParameter("userId", userId);
      session.flush();
      session.clear();
      return query.list();
    }
  }
  * */
}
