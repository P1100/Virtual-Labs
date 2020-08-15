package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

/**
 * Corso
 * Il corso universitario è caratterizzato da un nome, un acronimo, e ad esso sono associati gli
 * studenti iscritti a quel corso. Il corso può essere attivo o spento (se è spento non si possono
 * utilizzare le corrispondenti macchine virtuali). Su ogni corso è impostata la dimensione minima e
 * massima di studenti che possono comporre un gruppo, per quel corso
 * <p>
 * Id is the course acronym. If course disabled, you cant use the VM associated
 */
@Entity
@Data
@ToString(exclude = {"teams", "students", "professors"})
public class Course {
  @Id
  private String id;
  @NotBlank
  private String fullName;
  @PositiveOrZero
  private int minEnrolled;
  @Positive
  private int maxEnrolled;
  @NotNull
  private boolean enabled;
  String pathModelVM;
  private int maxVcpu, maxDiskSpace, maxRam, maxRunningVM,
      maxTotVM; // sum of enabled and disabled
  @OneToMany(mappedBy = "course")
  private List<Team> teams = new ArrayList<>();
  @ManyToMany(mappedBy = "courses", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Student> students = new ArrayList<>();
  @ManyToMany(mappedBy = "courses", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Professor> professors = new ArrayList<>();
  
  public void addEnrollStudent(Student new_student) {
    students.add(new_student);
    new_student.getCourses().add(this);
  }
  
  public void removeDisenrollStudent(Student old_student) {
    students.remove(old_student);
    old_student.getCourses().remove(this);
  }
  
  public void addTeam(Team new_team) {
    new_team.setCourse(this);
  }
  
  public void removeTeam(Team old_team) {
    teams.remove(old_team);
    old_team.setCourse(null);
  }
}
