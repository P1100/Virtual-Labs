package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
 * IMPORTANT! Didn't use group, instead of team, because 'group' is a reserved MySQL word
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
  private boolean active = false; // status
  @PositiveOrZero
  private int maxVcpu, maxDisk, maxRam, maxRunningVM, maxTotVM; // sum of enabled and disabled

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
    if (course != null)
      course.getTeams().remove(this);
    if (new_course == null) {
      course = null;
    } else {
      new_course.getTeams().add(this);
      course = new_course;
    }
  }

  public void addStudent(Student new_student) {
    members.add(new_student);
    new_student.getTeams().add(this);
  }
}