package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomCourseRepositoryImpl implements CustomCourseRepository {
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public List<Course> advancedSearchCourses() {
    
    StringBuilder jpql = new StringBuilder();
    jpql.append("from Course c where c.enabled=true");
    
    Map<String, Object> parameters = new HashMap<String, Object>();
//
//    jpql.append("OR c.max>:max");
//    parameters.put("name", 100);
//    jpql.append("OR c.name like :name");
//    parameters.put("name", "_9");
    
    TypedQuery<Course> query = entityManager.createQuery(jpql.toString(), Course.class);

//    parameters.forEach((key, value) -> query.setParameter(key, value));
    
    return query.getResultList();
  }
}
