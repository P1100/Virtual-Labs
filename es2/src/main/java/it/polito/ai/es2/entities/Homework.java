package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Homework {
  public enum Status {NULL, REAAD, SUBMITTED, REVIEWED, ENDED}
  
  @Id
  private Long id;
  private Status status = Status.NULL; //initial state
  private Boolean permanent;
  private String grade;
  @ManyToOne
  @JoinColumn
  private Student student;
  @ManyToOne
  @JoinColumn
  private Assignment assignment; // --> obtain from it: course, professor
  @OneToMany(mappedBy = "submission")
  private List<Image> imageSubmissions;
}
