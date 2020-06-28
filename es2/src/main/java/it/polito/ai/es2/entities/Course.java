package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Course {
  // changed from "name" to "idname", since it was confusing me. Note: refractoring this field doesnt work very well, dont ever do it again.
  @Id
  private String name;
  private int min;
  private int max;
  boolean enabled;
  String professor;
  @OneToMany(mappedBy = "course")
  @ToString.Exclude
  List<Group> groups = new ArrayList<>();
  @ManyToMany(mappedBy = "courses") //cascade = CascadeType.ALL, orphanRemoval = true
  @ToString.Exclude
  private List<Student> students = new ArrayList<>();
  
  public void addStudent(Student new_student) {
    students.add(new_student);
    new_student.getCourses().add(this);
  }
  
  public void removeStudent(Student old_student) {
    students.remove(old_student);
    old_student.getCourses().remove(this);
  }
  
  public void addTeam(Group new_group) {
    new_group.setCourse(this);
  }
  
  public void removeTeam(Group old_group) {
    groups.remove(old_group);
    old_group.setCourse(null);
  }
}
