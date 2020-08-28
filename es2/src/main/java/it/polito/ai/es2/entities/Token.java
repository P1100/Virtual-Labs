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
  // TODO: no need to link to team table? Review later
  private Long teamId;
  private Timestamp expiryDate;

  // TODO: ??? Link to student other way?
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  Student student;
}
