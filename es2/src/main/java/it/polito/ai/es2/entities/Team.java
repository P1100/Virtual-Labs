package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

/**
 * Gruppo
 * Il gruppo corrisponde ad un gruppo di studenti, ha un nome, un identificativo ed è associato ad
 * uno e uno solo ​modello ​di macchina virtuale e a uno e uno solo corso. Non ci possono essere due
 * gruppi con lo stesso nome all’interno dello stesso corso.
 * Per ciascun gruppo il docente imposta un limite di risorse utilizzabili in termini di numero di vcpu,
 * spazio disco e ram, numero di istanze attive contemporaneamente e numero massimo di istanze
 * disponibili (cioè somma di quelle attive e spente).
 * <p>
 * Didn"t use group, instead of team, because 'group' is a reserved MySQL word
 */
@Data
@EqualsAndHashCode(of = {"course", "id", "name"})
@Entity
public class Team {
  @Id
  @GeneratedValue
  private Long id;
  @NotBlank
  private String name;
  @Range(min = 0, max = 1)
  private int status = 0; //0 inactive, 1 active
  @PositiveOrZero
  private int maxVcpu, maxDisk, maxRam, maxRunningVM, maxTotVM; // sum of enabled and disabled
  
  public static int status_inactive() {
    return 0;
  }
  
  public static int status_active() {
    return 1;
  }
  
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
  @JoinColumn(name = "course_id")
  Course course; // --> model vm
  
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  List<Student> members = new ArrayList<>();
  
  @OneToMany(mappedBy = "team")
  private List<VM> vms = new ArrayList<>();
  
  
  public void setCourse(Course new_course) {
    if (this.course != null)
      this.course.getTeams().remove(this);
    if (new_course == null) {
      this.course = null;
    } else {
      new_course.getTeams().add(this);
      this.course = new_course;
    }
  }
  
  public void addStudent(Student new_student) {
    members.add(new_student);
    new_student.getTeams().add(this);
  }
  
  public void removetudent(Student old_student) {
    old_student.getTeams().remove(this);
    this.members.remove(old_student);
  }
}