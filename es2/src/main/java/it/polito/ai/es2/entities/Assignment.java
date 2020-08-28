package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class Assignment {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  @PastOrPresent
  private Timestamp releaseDate;
  @FutureOrPresent
  private Timestamp expireDate;

  @OneToOne(optional = false)
  @JoinColumn
  @NotNull
  private Image content;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn
  @NotNull
  private Course course;

  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  @JoinColumn
  @NotNull
  private Professor creator;

  @OneToMany(mappedBy = "assignment")
  private List<Implementation> implementations;

  // Parameters and initialization needed at entity creation
  void initNewEntity(Course c, Professor p, Image i) {
    course = c;
    c.getAssignments().add(this);
    creator = p;
    p.getAssignments().add(this);
    content = i;
    i.setAssignment(this);
  }
}
