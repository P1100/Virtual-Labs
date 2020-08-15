package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class VM {
  @Id
  private Long id;
  private int vcpu;
  private int diskSpace;
  private int ram;
  private boolean active;
  @ManyToOne
  private Team team; // get course from team
  @ManyToMany
  private List<Student> owners = new ArrayList<>();
  @OneToOne
  private Image screenshotVM;
}
