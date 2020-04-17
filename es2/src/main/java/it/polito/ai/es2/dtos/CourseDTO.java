package it.polito.ai.es2.dtos;

import lombok.Data;

import javax.persistence.Id;

@Data
public class CourseDTO {
    boolean enabled;
    @Id
    private String name;
    private int min;
    private int max;
}
