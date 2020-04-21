package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Team {/*
    @Override
    public String toString() {
        return "Team{}";
    }*/

    @Id
    @GeneratedValue
    Long id;
    String name;
    int status;
    @ManyToOne//(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    Course course;

    public Team(String name, int status) {
        super();
        this.name = name;
        this.status = status;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
