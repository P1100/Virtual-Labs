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
  private String name;
  private Timestamp releaseDate;
  private Timestamp expireDate;
  @OneToOne(optional = false)
  @JoinColumn
  private Image content;
  
  @ManyToOne
  @JoinColumn
  private Course course;
  
  @ManyToOne(optional = false)
  @JoinColumn
  private Professor creator;
  
  @OneToMany(mappedBy = "assignment")
  private List<Implementation> implementations;
  
  // Parameters needed at creation
  void init(Course c, Professor p, Image i) {
    course = c;
    c.getAssignments().add(this);
    creator = p;
    p.getAssignments().add(this);
    content = i;
    i.setAssignment(this);
  }
}
