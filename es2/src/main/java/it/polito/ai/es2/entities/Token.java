package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Token {
  @Id
  String id;
  Long teamId;
  Timestamp expiryDate;
}
