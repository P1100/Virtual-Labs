package it.polito.ai.es2.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Course {
  @Id
  @NotBlank
  private String id; // acronym. Equals and Hashcode on lowercase value
  @NotBlank
  private String fullName;
  @PositiveOrZero
  private int minSizeTeam;
  @Positive
  private int maxSizeTeam;
  private boolean enabled;
  private String vmModelPath;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "course_student") // otherwise jpa-ql console wont find it
  private List<Student> students = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable
  private List<Professor> professors = new ArrayList<>();

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<Team> teams = new ArrayList<>(); // -> vms

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<Assignment> assignments = new ArrayList<>();

  public void addStudent(Student new_student) {
    students.add(new_student);
    new_student.getCourses().add(this);
  }

  public void removeStudent(Student old_student) {
    students.remove(old_student);
    old_student.getCourses().remove(this);
  }

  public void addProfessor(Professor p) {
    professors.add(p);
    p.getCourses().add(this);
  }

  public void removeProfessor(Professor p) {
    professors.remove(p);
    p.getCourses().remove(this);
  }

  @Override
  public String toString() {
    return "Course{} " + id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Course)) return false;
    Course course = (Course) o;
    return id.equals(course.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id.toLowerCase());
  }
}
