package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.util.ArrayList;
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
  private Timestamp expireDate;

  @OneToOne
  @JoinColumn
  private Image content;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Course course;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Professor creator;

  @OneToMany(mappedBy = "assignment")
  private List<Implementation> implementations = new ArrayList<>();

  // Parameters and initialization needed at entity creation
  void initNewEntity(Course c, Professor p, Image i) {
    course = c;
    c.getAssignments().add(this);
    creator = p;
    p.getAssignments().add(this);
    content = i;
    i.setAssignment(this);
  }

  @Override public String toString() {
    return "Assignment{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", releaseDate=" + releaseDate +
        ", expireDate=" + expireDate +
        '}';
  }
}
