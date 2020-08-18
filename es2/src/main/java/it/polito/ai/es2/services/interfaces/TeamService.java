package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import java.io.Reader;
import java.util.List;
import java.util.Optional;

// TODO: Security, move PreAuthorize statements here, on the the interface
public interface TeamService {
  boolean addCourse(CourseDTO course);
  Optional<CourseDTO> getCourse(String name);
  List<CourseDTO> getAllCourses();
  boolean addStudent(StudentDTO student);
  Optional<StudentDTO> getStudent(Long studentId);
  List<StudentDTO> getAllStudents();
  List<StudentDTO> getEnrolledStudents(String courseName);
  boolean enrollStudent(Long studentId, String courseName);
  void enableCourse(String courseName);
  void disableCourse(String courseName);
  List<Boolean> addAllStudents(List<StudentDTO> students);
  List<Boolean> enrollStudents(List<Long> studentIds, String courseName);
  List<Boolean> enrollStudentsCSV(Reader r, String courseName);
  List<CourseDTO> getCourses(Long studentId);
  List<TeamDTO> getTeamsForStudent(Long studentId);
  List<StudentDTO> getMembers(Long TeamId);
  TeamDTO proposeTeam(String courseId, String name, List<Long> memberIds);
  List<TeamDTO> getTeamsForCourse(String courseName);
  List<StudentDTO> getStudentsInTeams(String courseName);
  List<StudentDTO> getAvailableStudents(String courseName);

//  ------------- Added on es3 --------------
  
  List<TeamDTO> getAllTeams();
  Optional<TeamDTO> getTeam(Long teamId);
  boolean setTeamStatus(Long teamId, int status);
  boolean evictTeam(Long teamId);

//  ---------- Added after beginning project-----------------
  
  void testing();
  
  void disenrollStudent(Long studentId, String courseId);
  boolean updateCourse(CourseDTO courseDTO);
  boolean deleteCourse(String courseId);
//  boolean updateCourse();
//  boolean deleteCourse(); //cascade all di: teams, assignments, submissions...
}
