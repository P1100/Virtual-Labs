package it.polito.ai.es2.repositories;

public interface CourseCustomRepository {
  Integer countTeamsThatViolateCardinality(String courseId, int min, int max);
}
