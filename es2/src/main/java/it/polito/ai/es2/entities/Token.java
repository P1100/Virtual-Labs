package it.polito.ai.es2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
  @Id
  private String id;
  private Long teamId; // TODO: remove later
  @FutureOrPresent
  private Timestamp expiryDate;

  /* Unilateral relationships */
  @ManyToOne(optional = true)
  @JoinColumn
  private User user;
  // TODO: Must remove teamId first
//  @ManyToOne(optional = true)
//  @JoinColumn
//  private Team team;
}
