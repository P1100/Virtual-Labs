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

/**
 * Corso
 * Il corso universitario è caratterizzato da un nome, un acronimo, e ad esso sono associati gli
 * studenti iscritti a quel corso. Il corso può essere attivo o spento (se è spento non si possono
 * utilizzare le corrispondenti macchine virtuali). Su ogni corso è impostata la dimensione minima e
 * massima di studenti che possono comporre un gruppo, per quel corso
 * <p>
 * Id is the course acronym. If course disabled, you cant use the VM associated
 */
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

  @ManyToMany(mappedBy = "courses", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Student> students = new ArrayList<>();

  @ManyToMany(mappedBy = "courses", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
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

  public void addTeam(Team new_team) {
    new_team.setCourse(this);
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
    return id.equalsIgnoreCase(course.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id.toLowerCase());
  }
}
