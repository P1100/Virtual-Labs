package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "course_id"}))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team {
  public static int status_inactive() {
    return 0;
  }
  
  public static int status_active() {
    return 1;
  }
  
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue
  Long id;
  @EqualsAndHashCode.Include
  @NotBlank
  String name;
  int status;
  @Transient
  MultipartFile modelVM;
  Long maxVcpu;
  Long maxDiskSpace;
  Long maxRam;
  Long maxVmIstancesEnabled;
  Long maxVmIstancesAvailable; // sum of enabled and disabled
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "course_id")
  @EqualsAndHashCode.Include
  Course course;
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  @ToString.Exclude
  List<Student> members = new ArrayList<>();
  
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