package it.polito.ai.es2.repositories;

public interface CourseCustomRepository {
  Integer countTeamsThatViolateCardinality(String id, int min, int max);
}
