package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Each student can be associated with max one group per course
 */
@Data
@Entity
public class Student {
  @Id // matricola, serial
  private String id;
  @NotBlank
  private String name;
  private String firstName;
  // TODO: search later how to make this work
  @Transient
  private MultipartFile imageData;
  // TODO: check somewhere that for each course there is max one group associated
  @ManyToMany(mappedBy = "members", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @ToString.Exclude
  List<Team> teams = new ArrayList<>();
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
  
  public void removeTeam(@org.jetbrains.annotations.NotNull Team team) {
    team.getMembers().remove(this);
    teams.remove(team);
  }
}
