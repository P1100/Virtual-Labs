package it.polito.ai.es2.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Utente: Docente
 * Il docente dell’università è caratterizzato dall’email, da un nome, cognome, matricola e foto. Il
 * docente può essere associato a zero, uno o più corsi. E’ il docente stesso che crea i corsi e li
 * popola con gli studenti iscritti. Il docente non crea o gestisce macchine virtuali, ma può connettersi
 * a quelle degli studenti/gruppi dei corsi che gestisce. Attenzione: ci possono essere più docenti
 * gestori dello stesso corso
 * <p>
 * Id is the matricola/serial
 */
@Entity
public class Teacher {
  @Id
  private String id;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] profilePicture;
//  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//  @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"),
//      inverseJoinColumns = @JoinColumn(name = "course_name"))
//  @ToString.Exclude
//  private List<Course> courses = new ArrayList<>();
}
