package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Token {
  @Id
  private String id;
  // TODO: no need to link to team table? Review later
  private Long teamId;
  private Timestamp expiryDate;
}
