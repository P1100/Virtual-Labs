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
  private int disk;
  private int ram;
  private boolean active;
//  private String vmModel; // -> saved in course
  
  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  @JoinColumn
  private Team team; // --> course
  
  // TODO: check on add, students must be in same team
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn
  private Student creator;
  
  @ManyToMany(cascade = CascadeType.MERGE)
  @JoinTable
  private List<Student> sharedOwners = new ArrayList<>();
  
  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn
  private Image imageVm;
}
