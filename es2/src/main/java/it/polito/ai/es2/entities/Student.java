package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Utente: Studente<p>
 * Lo studente dell’università è caratterizzato dall’email @studenti.polito.it, dal nome, cognome,
 * matricola e foto.
 * <p>Lo studente, può essere associato massimo ad uno e un solo gruppo (per singolo corso),
 * può creare/cancellare/eseguire/arrestare/spegnere istanze di macchine virtuali di quel gruppo.
 * <p>Lo studente può essere iscritto a zero, uno o più corsi.
 */
@Data
@Entity
public class Student {
  @Id
  private String id;  // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  @Email
  private String email;
  /**
   * Multiple teams because each student might be enrolled in multiple courses at the same time
   */
  @ManyToMany(mappedBy = "members", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  List<Team> teams = new ArrayList<>();
  
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  private List<Course> courses = new ArrayList<>();
  
  @OneToOne
  @JoinColumn
  private Image profilePhoto;
  
  @ManyToMany(mappedBy = "studentOwners")
  private List<VM> vms;
  
  @OneToMany(mappedBy = "student")
  private List<Implementation> homeworks;
  
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
