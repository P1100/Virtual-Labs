package it.polito.ai.es2.securityconfig;

import it.polito.ai.es2.dtos.VmDTO;
import it.polito.ai.es2.entities.*;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.VMRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Example: @mySecurityChecker.isCourseOwner(#courseName,authentication.principal.username))
 */
@Service
//@Transactional // DONT! I dont need detached entities saved in this class (read only)
@Log
public class MySecurityChecker {
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  VMRepository vmRepository;

  public boolean isVmOwner(Long vmId, String principal_username) {
    Long studentId = Long.valueOf(principal_username);
    if (vmId == null || principal_username.isBlank())
      return false;
    Student student = studentRepository.findById(studentId).orElse(null);
    if (student == null) {
      return false;
    }
    VM savedVm = vmRepository.findById(vmId).orElse(null);
    if (savedVm == null)
      return false;
    // Changes to SharedOwners are not saved, without neither transactional nor save
    List<Student> owners = new ArrayList<>(savedVm.getSharedOwners());
    owners.add(savedVm.getCreator());
    return owners.stream().anyMatch(owner -> studentId.equals(owner.getId()));
  }
  public boolean isVmOwner(VmDTO vmDTO, String principal_username) {
    Long studentId = Long.valueOf(principal_username);
    if (vmDTO == null || principal_username.isBlank())
      return false;
    Student student = studentRepository.findById(studentId).orElse(null);
    if (student == null) {
      return false;
    }
    VM savedVm = vmRepository.findById(vmDTO.getId()).orElse(null);
    if (savedVm == null)
      return false;
    // Changes to SharedOwners are not saved, without neither transactional nor save
    List<Student> owners = new ArrayList<>(savedVm.getSharedOwners());
    owners.add(savedVm.getCreator());
    return owners.stream().anyMatch(owner -> studentId.equals(owner.getId()));
  }

  public boolean isStudentEnrolled(String courseId, String principal_username) {
    Long studentId = Long.valueOf(principal_username);
    if (courseId.isBlank() || principal_username.isBlank())
      return false;
    Student student = studentRepository.findById(studentId).orElse(null);
    if (student == null) {
      return false;
    }
    return student.getCourses().stream().map(Course::getId).anyMatch(s -> s.equals(courseId));
  }

  public boolean isCourseOwner(String course, String principal_username) {
    if (course.isBlank() || principal_username.isBlank())
      return false;
    List<Professor> teachers = courseRepository.findById(course).map(Course::getProfessors).orElse(new ArrayList<>());
    return teachers.stream().anyMatch(x -> principal_username.equals(x.getId().toString()));
  }

  public boolean isTeamOwner(Long id, String principal_username) {
    if (id == null || principal_username.isBlank())
      return false;
    List<Student> students = teamRepository.findById(id).map(Team::getStudents).orElse(null);
    if (students == null)
      return false;
    return students.stream().anyMatch(student -> student.getId().toString().equals(principal_username));
  }

  public boolean isOwner(String parameter, String principal_username) {
    if (parameter.isBlank() || principal_username.isBlank())
      return false;
    return parameter.equals(principal_username);
  }
}
