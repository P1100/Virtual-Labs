package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Course {
    @Id
    private String name;
    private int min;
    private int max;
    boolean enabled;
    @OneToMany(mappedBy = "course")
    List<Team> teams = new ArrayList<>();
    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
    
    public void addStudent(Student new_student) {
        students.add(new_student);
        new_student.getCourses().add(this);
    }
    
    public void removeStudent(Student old_student) {
        students.remove(old_student);
        old_student.getCourses().remove(this);
    }
    
    public void addTeam(Team new_team) {
        new_team.setCourse(this);
    }
    
    public void removeTeam(Team old_team) {
        teams.remove(old_team);
        old_team.setCourse(null);
    }
}
