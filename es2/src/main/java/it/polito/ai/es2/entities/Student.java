package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Student {
  @ManyToMany(mappedBy = "members")
  List<Team> teams = new ArrayList<>();
  @Id
  private String id;
  private String name;
  private String firstName;
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "course_name"))
  @ToString.Exclude
  private List<Course> courses = new ArrayList<>();
  
  public void addCourse(Course new_course) {
    courses.add(new_course);
    new_course.getStudents().add(this);
  }
  
  public void removeCourse(Course old_course) {
    courses.remove(old_course);
    old_course.getStudents().remove(this);
  }
  
  public void addTeam(Team team) {
    teams.add(team);
    team.getMembers().add(this);
  }
  
  public void removeTeam(Team team) {
    team.getMembers().remove(this);
    teams.remove(team);
  }
}
