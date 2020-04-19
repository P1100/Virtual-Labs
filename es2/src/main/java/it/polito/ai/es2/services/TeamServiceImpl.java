package it.polito.ai.es2.services;

import it.polito.ai.es2.exceptions.CourseNotFoundException;
import it.polito.ai.es2.exceptions.StudentNotFoundException;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log
public class TeamServiceImpl implements TeamService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CourseRepository cr;
    @Autowired
    StudentRepository sr;
//    List<CourseDTO> courseDTOList;
//    List<StudentDTO> studentDTOList;
//    public TeamServiceImpl() {
//        super();
//        courseDTOList = new ArrayList<>();
//        studentDTOList = new ArrayList<>();
//    }

    @Override
    public boolean addStudent(StudentDTO student) {
        if (student == null || student.getId() == null)
            return false;
        Student s = modelMapper.map(student, Student.class);
        try {
            if (!sr.existsById(student.getId())) {
                sr.save(s);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            log.warning("###### IllegalArgumentException:" + e.toString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.warning("###### Other Exception:" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addCourse(CourseDTO course) {
        if (course == null || course.getName() == null) {
            return false;
        }
        Course c = modelMapper.map(course, Course.class);
        try {
            if (!cr.existsById(course.getName())) {
                cr.save(c);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            log.warning("IllegalArgumentException:" + e.toString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.warning("###### Other Exception:" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<CourseDTO> getCourse(String name) {
        Optional<Course> byId = cr.findById(name);
        Course cc = byId.get();
//        Optional<CourseDTO> courseDTO = byId.map(x -> modelMapper.map(x, CourseDTO.class));
        Optional<CourseDTO> courseDTO = Optional.of(modelMapper.map(cc, CourseDTO.class));
        return courseDTO;
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return cr.findAll().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDTO> getStudent(String studentId) {
        return sr.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return sr.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
    }



    @Override
    public List<StudentDTO> getEnrolledStudents(String courseName) throws StudentNotFoundException {
        throw new StudentNotFoundException("StudentNotFoundException: getEnrolledStudents(" + courseName + ")");
//        return null;
    }

    @Override
    public boolean addStudentToCourse(String studentId, String courseName) throws StudentNotFoundException {
        return false;
    }

    @Override
    public void enableCourse(String courseName) throws CourseNotFoundException {

    }

    @Override
    public void disableCourse(String courseName) throws CourseNotFoundException {

    }

   /*
    @Override
    public boolean addStudentToCourse(String studentId, String courseName) {
        if (!getCourse(courseName).isPresent())
            throw new CourseNotFoundException();
        if (!getStudent(studentId).isPresent())
            throw new StudentNotFoundException();
        Course c = courseRepository.getOne(courseName);
        // TO DO: anyMatch(), equals() o compareTo
        if (c.getStudents().stream().anyMatch(x->x.getId().equals(studentId)))
            return false;
        Student s =  studentRepository.getOne(studentId);
        // paranoia
        if (s.getCourses().stream().anyMatch(x->x.getName().equals(courseName)))
            return false;
        c.addStudent(s);
        return true;
    }

    public void addStudent(Student s){
        students.add(s);
        s.getCourses().add(this);
    }
  */
}
