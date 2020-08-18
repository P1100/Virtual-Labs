package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class Implementation {
  public enum Status {NULL, READ, SUBMITTED, REVIEWED, DEFINITIVE}
  
  @Id
  private Long id;
  private Status status = Status.NULL; // initial state
  private Boolean permanent = false;
  private String grade;
  private Timestamp read_status, definitive_status; // cant use 'read' as column name, mariadb error
  
  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  @JoinColumn
  private Student student;
  
  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  @JoinColumn
  private Assignment assignment; // --> course, professor
  
  @OneToMany(mappedBy = "submission")
  private List<Image> imageSubmissions;
}
