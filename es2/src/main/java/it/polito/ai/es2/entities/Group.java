package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "course_id"}))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group {
  public static int status_inactive() {
    return 0;
  }
  
  public static int status_active() {
    return 1;
  }
  
  @Id
  @GeneratedValue
  Long id; // Long invece che long ci permette di avere campi null temporanei della chiave primaria!
  @EqualsAndHashCode.Include
  @NotBlank
  String name;
  int status;
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "course_id")
  @EqualsAndHashCode.Include
  Course course;
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  @ToString.Exclude
  List<Student> members = new ArrayList<>();
  
  public void setCourse(Course new_course) {
    if (this.course != null)
      this.course.getGroups().remove(this);
    if (new_course == null) {
      this.course = null;
    } else {
      new_course.getGroups().add(this);
      this.course = new_course;
    }
  }
  
  public void addStudent(Student new_student) {
    members.add(new_student);
    new_student.getGroups().add(this);
  }
  
  public void removetudent(@org.jetbrains.annotations.NotNull Student old_student) {
    old_student.getGroups().remove(this);
    this.members.remove(old_student);
  }
}