package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
//    @Query("SELECT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.name= :courseName")
//    List<Student> getStudentsInTeams(String courseName);

    // TODO: Query
//    List<Student> getStudentsNotInTeams(String courseName);
}
