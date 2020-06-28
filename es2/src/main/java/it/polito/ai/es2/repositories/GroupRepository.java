package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
  List<Group> findAllByNameAndCourse_name(String name, String courseId);
  
  Group findFirstByNameAndCourse_name(String name, String courseId);
  
  Group findFirstByNameAndStatusIsAndCourse_name(String name, int status, String courseId);
}
