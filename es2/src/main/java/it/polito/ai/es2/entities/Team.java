package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"course", "id", "name"})
@Entity
public class Team {
  @Id
  @GeneratedValue
  private Long id;
  @NotBlank
  private String name;
  private boolean active = false; // false for proposals
  private boolean disabled = false;
  @PositiveOrZero
  private int maxVcpu, maxDisk, maxRam, maxRunningVm, maxTotVm; // sum of enabled and disabled
  @CreationTimestamp
  private Timestamp createdDate;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
  @JoinColumn(name = "course_id", nullable = false)
  Course course; // --> model vm

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  List<Student> students = new ArrayList<>(); // Proposer is the first added!

  @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
  private List<Token> tokens = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  private List<VM> vms = new ArrayList<>();

  public void addSetCourse(Course new_course) {
    if (course != null)
      throw new RuntimeException("JPA-Team: overriding a OneToOne or ManyToOne field might be an error");
    new_course.getTeams().add(this);
    course = new_course;
  }

  public void addStudent(Student new_student) {
    students.add(new_student);
    new_student.getTeams().add(this);
  }

  public void removeStudent(Student x) {
    students.remove(x);
    x.getTeams().remove(this);
  }

  public void removeToken(Token t) {
    tokens.remove(t);
    t.setTeam(null);
  }

  @Override public String toString() {
    return "Team{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", active=" + active +
        ", disabled=" + disabled +
        ", maxVcpu=" + maxVcpu +
        ", maxDisk=" + maxDisk +
        ", maxRam=" + maxRam +
        ", maxRunningVM=" + maxRunningVm +
        ", maxTotVM=" + maxTotVm +
        ", createdDate=" + createdDate +
        '}';
  }
}