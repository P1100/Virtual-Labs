package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Student {
    @Id
    private String id;
    private String name;
    private String firstName;
    // TODO lasciare solo persist e merge
    @ManyToMany(mappedBy = "members", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    List<Team> teams = new ArrayList<>();
    // TODO lasciare solo persist e merge
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_name"))
    private List<Course> courses = new ArrayList<>();

    // TODO never used? Controllare
    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void addTeam(Team team) {
        teams.add(team);
        team.getMembers().add(this);
    }

    public void removeTeam(Team team) {
        team.getMembers().remove(this);
        teams.remove(team);
    }
}
