package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Corso
 * Il corso universitario è caratterizzato da un nome, un acronimo, e ad esso sono associati gli
 * studenti iscritti a quel corso. Il corso può essere attivo o spento (se è spento non si possono
 * utilizzare le corrispondenti macchine virtuali). Su ogni corso è impostata la dimensione minima e
 * massima di studenti che possono comporre un gruppo, per quel corso
 * <p>
 * Id is the course acronym. If disabled you cant use the VM associated
 */
@Entity
@Data
public class Course {
  @Id
  private String id;
  private String fullName;
  private int minEnrolled;
  private int maxEnrolled;
  private boolean enabled;
  @OneToMany(mappedBy = "course")
  @ToString.Exclude
  List<Team> teams = new ArrayList<>();
  @ManyToMany(mappedBy = "courses") //cascade = CascadeType.ALL, orphanRemoval = true
  @ToString.Exclude
  private List<Student> students = new ArrayList<>();
  @ManyToMany(mappedBy = "courses")
  @ToString.Exclude
  private List<Teacher> teachers = new ArrayList<>();
  
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
