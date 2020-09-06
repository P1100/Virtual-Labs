package it.polito.ai.es2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
  @Id
  private String id;
  @FutureOrPresent
  private Timestamp expiryDate;
  /* Only for team */
  private boolean confirmed = false;
  private String urlConfirm;
  private String urlReject;
  private boolean rejected = false;

  @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = true)
  @JoinColumn(nullable = true)
  private User user; // registration token

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn
  private Team team; // team token
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn
  private Student student;

  public void addSetUser(User savedUser) {
    if (user != null)
      throw new RuntimeException("JPA-Token: overriding a OneToOne or ManyToOne field might be an error");
    user = savedUser;
    savedUser.setTokenSignup(this);
  }

  public void addSetStudent(Student s) {
    if (student != null)
      throw new RuntimeException("JPA-Token: overriding a OneToOne or ManyToOne field might be an error");
    student = s;
    s.getTokens().add(this);
  }

  public void addSetTeam(Team t) {
    if (user != null)
      throw new RuntimeException("JPA-Token: overriding a OneToOne or ManyToOne field might be an error");
    team = t;
    team.getTokens().add(this);
  }
}
