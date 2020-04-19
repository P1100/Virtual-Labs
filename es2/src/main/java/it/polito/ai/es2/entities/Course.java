package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Course {
    boolean enabled;
    @Id
    private String name;
    private int min;
    private int max;

    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY) //FetchType.EAGER
    private List<Student> students = new ArrayList<>(); // ->  use Set

    private void addStudent(Student student) {
        students.add(student);
        student.getCourses().add(this);
    }
/*
    @Entity
    @Data
    public class Course {
        @Id
        String name;
        int min;
        int max;
        Boolean enabled;
        @ManyToMany(mappedBy = "courses")
        List<Student> students = new ArrayList<>();
        *//*
       @OneToMany(mappedBy="course")
       List<Team> teams;
       *//*
        public void addStudent(Student s){
            students.add(s);
            s.getCourses().add(this);
        }
*//*
    public void addTeam(Team t){
       teams.add(t);
       t.setCourse(this);
    }
     *//*
    }
    */
}
