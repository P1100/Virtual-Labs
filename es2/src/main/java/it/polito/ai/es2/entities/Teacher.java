package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
@Entity
@Data
public class Teacher {
  @Id
  private String id; // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String email;
  //  @Lob
//  @Basic(fetch = FetchType.LAZY)
//  private byte[] profilePhoto;
  private String profilePhoto;
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "teacher_course", joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  @ToString.Exclude
  private List<Course> courses = new ArrayList<>();
}
