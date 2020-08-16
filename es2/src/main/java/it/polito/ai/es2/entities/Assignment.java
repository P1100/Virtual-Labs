package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class Assignment {
  @Id
  @GeneratedValue
  private Long id;
  private Timestamp releaseDate;
  private Timestamp expireDate;
  @ManyToOne
  @JoinColumn
  private Professor owner;
  // TODO: redundant?
//  @ManyToOne
//  @JoinColumn
//  private Course course;
  @OneToOne(mappedBy = "")
  private Image contentAssignment;
  @OneToMany(mappedBy = "assignment")
  private List<Homework> homeworks;
}
