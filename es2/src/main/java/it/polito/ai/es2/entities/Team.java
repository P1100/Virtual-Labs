package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Team {
  @Id
  @GeneratedValue
  Long id;
  String name;
  int status;
  // TODO lasciare solo persist e merge
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "course_id")
  @ToString.Exclude
  Course course;
  // TODO lasciare solo persist e merge
  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  @ToString.Exclude
  List<Student> members = new ArrayList<>();
  
  // TODO ricontrollare e aggiungere lista
  public void setCourse(Course new_course) {
    if (new_course == null) {
      this.course.getTeams().remove(this);
      this.course = null;
    } else {
      new_course.getTeams().add(this);
      this.course = new_course;
    }
  }
  
  /*public void setCourse(Course course) {
      this.course.getTeams().remove(this);
      if(course!=null)
          course.getTeams().add(this);
      this.course = course;
  }*/
  public void addStudent(Student new_student) {
    members.add(new_student);
    new_student.getTeams().add(this);
  }
  
  public void removetudent(Student old_student) {
    old_student.getTeams().remove(this);
    this.members.remove(old_student);
  }
}
