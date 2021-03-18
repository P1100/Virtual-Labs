package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

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
