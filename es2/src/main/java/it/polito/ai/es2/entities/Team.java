package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Team {
    @Id
    @GeneratedValue
    Long id;
    String name;
    int status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    Course course;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY) //FetchType.EAGER
    @JoinTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    List<Student> members = new ArrayList<>();

    public void setCourse(Course course) {
        if (course == null) {
            this.course.getTeams().remove(this);
        } else
            this.course = course;
    }

    public void addStudent(Student student) {
        members.add(student);
        student.getTeams().add(this);
    }

    public void removetudent(Student student) {
        student.getTeams().remove(this);
        this.members.remove(student);
    }
}
