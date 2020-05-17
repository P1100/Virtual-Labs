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
public class Team {
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
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER) // TODO: remove eager later, it's for testing in commandline
  @JoinColumn(name = "course_id") // TODO: aggiungere nullable = false ?
  @EqualsAndHashCode.Include
  Course course;
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
  // TODO: remove eager later, it's for testing in commandline
  @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  @ToString.Exclude
      List<Student> members = new ArrayList<>();
  
  public void setCourse(Course new_course) {
    if (this.course != null)
      this.course.getTeams().remove(this);
    if (new_course == null) {
      this.course = null;
    } else {
      new_course.getTeams().add(this);
      this.course = new_course;
    }
  }
  
  public void addStudent(Student new_student) {
    members.add(new_student);
    new_student.getTeams().add(this);
  }
  
  public void removetudent(@org.jetbrains.annotations.NotNull Student old_student) {
    old_student.getTeams().remove(this);
    this.members.remove(old_student);
  }
}