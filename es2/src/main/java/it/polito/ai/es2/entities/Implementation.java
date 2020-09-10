package it.polito.ai.es2.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Implementation {
  public enum Status {NULL, READ, SUBMITTED, REVIEWED, DEFINITIVE}

  @Id
  @GeneratedValue
  private Long id;
  private Status status = Status.NULL; // initial state
  private Boolean permanent = false;
  @Length(max = 3)
  private String grade;
  @PastOrPresent
  private Timestamp readStatus, definitiveStatus; // cant use 'read' as column name, reserved word: mariadb error
  private String currentCorrection;

  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  @JoinColumn(nullable = false)
  private Student student;

  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  @JoinColumn(nullable = false)
  private Assignment assignment; // --> course, professor

  @OneToMany(mappedBy = "submission")
  private List<Image> imageSubmissions = new ArrayList<>();
}
