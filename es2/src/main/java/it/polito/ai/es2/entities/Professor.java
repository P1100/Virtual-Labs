package it.polito.ai.es2.entities;

import lombok.Data;

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

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private Image profilePhoto;

  @ManyToMany(mappedBy = "professors", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Course> courses = new ArrayList<>(); // --> teams, vms

  @OneToMany(mappedBy = "creator", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Assignment> assignments = new ArrayList<>();

  @OneToOne(mappedBy = "professor")
  private User user;

  public void addSetProfilePhoto(Image x) {
    if (profilePhoto != null)
      throw new RuntimeException("JPA-Team: overriding a OneToOne or ManyToOne field might be an error");
    profilePhoto = x;
    x.setProfessor(this);
  }

  @Override
  public String toString() {
    return "Professor{} " + id + getLastName();
  }

  public void addImage(Image image) {
    profilePhoto = image;
    image.setProfessor(this);
  }
}
