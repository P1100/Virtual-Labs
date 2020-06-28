package it.polito.ai.es2.repositories;

import it.polito.ai.es2._provecodicelearning.MyTestingCustomCourseRepository;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// controllare CASE SENSITIVE, eventualmente modificare con Repository.findByIdIgnoreCase(String id)
@Repository
public interface CourseRepository extends JpaRepository<Course, String>, MyTestingCustomCourseRepository {
  @Query("SELECT DISTINCT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.id= :coursename")
  List<Student> getStudentsInTeams(String coursename);
  
  @Query("SELECT DISTINCT s FROM Student s WHERE s.id NOT IN (SELECT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.id= :coursename)")
  List<Student> getStudentsNotInTeams(String coursename);
}
