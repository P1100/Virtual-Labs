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

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY) //FetchType.EAGER
    @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_name"))
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }
        /*
    public void addTeam(Team t){
        teams.add(t);
        t.getMembers().add(this);
    }
    public void removeTeam(Team t){
        t.getMembers().remove(this);
        teams.remove(t);
    }
     */
}
