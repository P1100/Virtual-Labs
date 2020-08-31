package it.polito.ai.es2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
  @Id
  private String id; // TODO: lowercase it
  private Long teamId;
  private Timestamp expiryDate;

  // Remove and change to unique field userId (@UniqueElements)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn
  Student student;
}
