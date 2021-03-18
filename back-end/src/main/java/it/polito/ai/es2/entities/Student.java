package it.polito.ai.es2.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "profilePhoto")
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
  /* Convenient values related to a Team and Course */
  @Transient
  private String teamName;
  @Transient
  private boolean proposalAccepted;
  @Transient
  private String urlTokenConfirm;
  @Transient
  private String urlTokenReject;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private Image profilePhoto;

  @ManyToMany(mappedBy = "students")
  @UniqueElements
  private List<Course> courses = new ArrayList<>();

  /**
   * Multiple teams because each student might be enrolled in multiple courses at the same time
   */
  @ManyToMany(mappedBy = "students")
  List<Team> teams = new ArrayList<>(); // --> vms total

  @OneToMany(mappedBy = "creator")
  private List<VM> vmsCreated;

  @ManyToMany(mappedBy = "sharedOwners")
  private List<VM> vmsOwned;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  private List<Implementation> implementations; // --> assignment

  @OneToOne(mappedBy = "student")
  private User user;

  @OneToMany(mappedBy = "student")
  private List<Token> tokens = new ArrayList<>();

  public void addImage(Image image) {
    profilePhoto = image;
    image.setStudent(this);
  }

  @Override public String toString() {
    return "Student{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", teamName='" + teamName + '\'' +
        ", proposalAccepted=" + proposalAccepted +
        ", urlTokenConfirm='" + urlTokenConfirm + '\'' +
        ", urlTokenReject='" + urlTokenReject + '\'' +
        '}';
  }
}
