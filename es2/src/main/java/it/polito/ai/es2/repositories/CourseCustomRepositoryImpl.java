package it.polito.ai.es2.repositories;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CourseCustomRepositoryImpl implements CourseCustomRepository {
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public Integer countTeamsThatViolateCardinality(String id, int min, int max) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", id);
    parameters.put("min", min);
    parameters.put("max", max);
    
    StringBuilder jpql = new StringBuilder();
    jpql.append("select count(*) as countViolatedTeams ");
    // JPQL, name of entity field (t.course). t.course_id used in nativeQuery
    jpql.append("from Team t where t.course.id = :id ");
    jpql.append("and :min > ANY(select count(*) from Team t2 where t2.course.id = :id group by t2.course) ");
    jpql.append("and :max < ANY(select count(*) from Team t3 where t3.course.id = :id group by t3.course)");
    TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
    parameters.forEach(query::setParameter);
    
    Long querySingleResult = null;
    try {
      querySingleResult = query.getSingleResult();
    } catch (NoResultException e) {
      return 0;
    }
    System.out.println("Return Query:" + querySingleResult);
    return querySingleResult.intValue();
  }
}
