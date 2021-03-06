package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  List<Assignment> findAllByCourse_Id(String courseId);
}
