package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// TODO: implementare metodi
@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    @Autowired
    CourseRepository cr;

    @Override
    public boolean addCourse(CourseDTO course) {
        return true;
//       return cr.exists(course);
    }

    @Override
    public Optional<CourseDTO> getCourse(String name) {
        return Optional.empty();
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return null;
    }

    @Override
    public boolean addStudent(StudentDTO student) {
        return false;
    }

    @Override
    public Optional<StudentDTO> getStudent(String studentId) {
        return Optional.empty();
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return null;
    }

    @Override
    public List<StudentDTO> getEnrolledStudents(String courseName) {
        return null;
    }

    @Override
    public boolean addStudentToCourse(String studentId, String courseName) {
        return false;
    }

    @Override
    public void enableCourse(String courseName) {

    }

    @Override
    public void disableCourse(String courseName) {

    }
}
