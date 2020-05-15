package it.polito.ai.es2._provecodicelearning;

import it.polito.ai.es2.entities.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MyTestingCustomCourseRepositoryImpl implements MyTestingCustomCourseRepository {
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public List<Course> advancedSearchCourses() {
    
    StringBuilder jpql = new StringBuilder();
    jpql.append("from Course c where c.enabled=true ");
    
    Map<String, Object> parameters = new HashMap<>();
    
    jpql.append(" OR c.max > :max ");
    parameters.put("max", 100);
    jpql.append(" OR c.name like :name ");
    parameters.put("name", "_9");
    
    TypedQuery<Course> query = entityManager.createQuery(jpql.toString(), Course.class);
    
    parameters.forEach(query::setParameter); //parameters.forEach((key, value) -> query.setParameter(key, value));
    
    return query.getResultList();
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
