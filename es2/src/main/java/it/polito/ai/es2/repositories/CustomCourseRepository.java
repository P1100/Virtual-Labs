package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Course;

import java.util.List;

public interface CustomCourseRepository {
  List<Course> advancedSearchCourses();
}
