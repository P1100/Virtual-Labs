package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Implementation {
  public enum Status {NULL, READ, SUBMITTED, REVIEWED, ENDED}
  
  @Id
  private Long id;
  private Status status = Status.NULL; // initial state
  private Boolean permanent = false;
  private String grade;
  
  @ManyToOne(optional = false)
  @JoinColumn
  private Student student;
  
  @ManyToOne(optional = false)
  @JoinColumn
  private Assignment assignment; // --> course, professor
  
  @OneToMany(mappedBy = "submission")
  private List<Image> imageSubmissions;
}
