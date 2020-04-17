package it.polito.ai.es2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private List<Student> students = new ArrayList<>();

    private boolean addStudent(Student student) {
        return this.students.add(student);
    }
}
