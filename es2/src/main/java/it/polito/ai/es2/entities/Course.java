package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    List<Team> Teams = new ArrayList<>();

    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Student> students = new ArrayList<>(); // ->  use Set

    public void addStudent(Student student) {
        students.add(student);
        student.getCourses().add(this);
    }

    public void addTeam(Team team) {
        Teams.add(team);
        team.setCourse(this);
    }

    public void removeTeam(Team team) {
        Teams.remove(team);
        team.setCourse(null);
    }
}
