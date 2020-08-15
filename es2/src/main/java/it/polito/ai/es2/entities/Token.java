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
// TODO: synch methods??
public class Token {
  @Id
  private String id;
  private Long teamId;
  private Timestamp expiryDate;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  private Student student;
}
