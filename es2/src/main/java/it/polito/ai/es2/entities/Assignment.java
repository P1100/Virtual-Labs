package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

@Data
@Entity
public class Assignment {
  @Id
  @GeneratedValue
  private Long id;
  private Timestamp releaseDate;
  private Timestamp expireDate;
  @OneToOne(mappedBy = "")
  private Professor professor;
  @OneToOne(mappedBy = "")
  private Course course;
  @OneToOne(mappedBy = "")
  private Image textAssignment;
}
