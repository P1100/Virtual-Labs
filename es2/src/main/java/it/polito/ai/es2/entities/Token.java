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
  String id;
  Long teamId;
  Timestamp expiryDate;
  @ManyToOne(fetch = FetchType.EAGER) // TODO: remove eager later, it's for testing in commandline
  @JoinColumn(name = "student_id_redundant", referencedColumnName = "id")
  Student student;
}
