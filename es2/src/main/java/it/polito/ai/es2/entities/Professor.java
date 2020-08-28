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
 * Utente: Docente
 * Il docente dell’università è caratterizzato dall’email, da un nome, cognome, matricola e foto. Il
 * docente può essere associato a zero, uno o più corsi. E’ il docente stesso che crea i corsi e li
 * popola con gli studenti iscritti. Il docente non crea o gestisce macchine virtuali, ma può connettersi
 * a quelle degli studenti/gruppi dei corsi che gestisce. Attenzione: ci possono essere più docenti
 * gestori dello stesso corso
 */
@Data
@Entity
public class Professor {
  @Id
  @PositiveOrZero
  private Long id; // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  @Email
  @Pattern(regexp = "d[0-9]{1,9}@polito\\.it")
  private String email;
  @OneToOne
  @JoinColumn
  private Image profilePhoto;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable
  @UniqueElements
  private List<Course> courses = new ArrayList<>(); // --> teams, vms

  @OneToMany(mappedBy = "creator", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Assignment> assignments;

  @Override
  public String toString() {
    return "Professor{} " + id + getLastName();
  }
}
