package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Utente: Studente
 * Lo studente dell’università è caratterizzato dall’email @studenti.polito.it, dal nome, cognome,
 * matricola e foto. Lo studente, può essere associato ad uno e un solo ​gruppo​, può
 * creare/cancellare/eseguire/arrestare/spegnere istanze di macchine virtuali di quel gruppo. Lo
 * studente può essere iscritto a zero, uno o più corsi
 * <p>
 * Each student can be associated with max one group per course. Id is the matricola/serial
 */
@Data
@Entity
public class Student {
  @Id
  private String id;
  @NotBlank
  private String firstName;
  @NotBlank
  private String name;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] profilePicture;
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
