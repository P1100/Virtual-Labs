package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Course;

import java.util.List;

public interface CourseCustomRepository {
  List<Course> advancedSearchCourses();
}
