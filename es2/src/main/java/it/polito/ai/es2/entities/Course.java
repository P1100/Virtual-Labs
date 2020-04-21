package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Course {/*
    @Override
    public String toString() {
        return "Course{}";
    }*/

    @Id
    private String name;

    private int min;
    private int max;
    boolean enabled;
    @OneToMany(mappedBy = "course")//, fetch = FetchType.EAGER)
            List<Team> teams = new ArrayList<>();

    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Student> students = new ArrayList<>(); // ->  use Set

    public Course(String name, int min, int max, boolean enabled) {
        super();
        this.name = name;
        this.min = min;
        this.max = max;
        this.enabled = enabled;
        this.teams = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
        student.getCourses().add(this);
    }

    public void addTeam(Team team) {
        teams.add(team);
        team.setCourse(this);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
        team.setCourse(null);
    }
}
