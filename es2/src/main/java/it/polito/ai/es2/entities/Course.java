package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
@Data
@ToString(exclude = {"teams", "students", "professors"})
@Entity
public class Course {
  @Id
  @NotBlank
  private String id; // acronym
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
  
  public void removeTeam(Team old_team) {
    teams.remove(old_team);
    old_team.setCourse(null);
  }

//  public void addAssignment(Assignment a) {
//    assignments.add(a);
//    a.setCourse(this);
//  }
}
