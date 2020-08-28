package it.polito.ai.es2.repositories;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CourseCustomRepositoryImpl implements CourseCustomRepository {
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public Integer countTeamsThatViolateCardinality(String courseId, int min, int max) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", courseId);
    parameters.put("min", min);
    parameters.put("max", max);
  
    Query query = entityManager.createNativeQuery(
        "SELECT count(*) FROM (SELECT ts.team_id, count(*) as c FROM virtuallabs.teams_students ts GROUP BY ts.team_id) tc " +
            "JOIN virtuallabs.team t ON tc.team_id = t.id WHERE t.course_id = :id " +
            "AND (tc.c < :min OR tc.c > :max)");
    parameters.forEach(query::setParameter);
  
    int querySingleResult;
    try {
      Object singleResult = query.getSingleResult();
      querySingleResult = ((BigInteger) singleResult).intValue();
    } catch (NoResultException Exception) {
      return 0;
    }
    return querySingleResult;
  }
}
