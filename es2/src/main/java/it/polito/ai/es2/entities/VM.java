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
//  private String vmModel; // -> saved in course
  @ManyToOne
  @JoinColumn
  private Team team; // -> obtain course from team
  // TODO: check on add, students must be in same team
  @ManyToMany(mappedBy = "vms")
  @JoinTable
  private List<Student> studentOwners = new ArrayList<>();
  @OneToOne
  @JoinColumn
  private Image imageVm;
}
