package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>, CourseCustomRepository {
  @Query("SELECT DISTINCT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.id= :courseId AND t.active= true")
  List<Student> getStudentsInTeams(String courseId);

  @Query("SELECT DISTINCT s FROM Student s WHERE s.id NOT IN (SELECT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.id= :courseId AND t.active= true)")
  List<Student> getStudentsNotInTeams(String courseId);

  // CustomRepository: Integer countTeamsThatViolateCardinality(String courseId, int min, int max);
}
