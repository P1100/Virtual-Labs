package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
  List<Team> findAllByNameAndCourse_id(String name, String courseId);

  Team findFirstByNameAndCourse_id(String name, String courseId);

  List<Team> findAllByActiveIsFalseAndDisabledIsTrue();

  Team findFirstByNameAndActiveIsAndCourse_id(String name, boolean status, String courseId);
}
