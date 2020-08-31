package it.polito.ai.es2.entities;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
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
  @PositiveOrZero
  private Long id;  // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  @Email
  @Pattern(regexp = "s[0-9]{1,9}@studenti\\.polito\\.it")
  private String email;
  @OneToOne
  @JoinColumn
  private Image profilePhoto;

  @ManyToMany
  @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  @UniqueElements
  private List<Course> courses = new ArrayList<>();

  /**
   * Multiple teams because each student might be enrolled in multiple courses at the same time
   */
  @ManyToMany(mappedBy = "members")
  List<Team> teams = new ArrayList<>(); // --> vms total

  @OneToMany(mappedBy = "creator")
  private List<VM> vmsCreated;

  @ManyToMany(mappedBy = "sharedOwners")
  private List<VM> vmsOwned;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  private List<Implementation> implementations; // --> assignment

  public void addTeam(Team team) {
    teams.add(team);
    team.getMembers().add(this);
  }

  @Override
  public String toString() {
    return "Student{} " + id + getLastName();
  }
}
