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

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
  @JoinColumn
  private User user;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
  @JoinColumn
  private Team team;

  public void addSetUser(User savedUser) {
    if (user != null)
      throw new RuntimeException("JPA-Team: overriding a OneToOne or ManyToOne field might be an error");
    user = savedUser;
    savedUser.getTokens().add(this);
  }

  public void addSetTeam(Team t) {
    if (user != null)
      throw new RuntimeException("JPA-Team: overriding a OneToOne or ManyToOne field might be an error");
    team = t;
    team.getTokens().add(this);
  }
}
