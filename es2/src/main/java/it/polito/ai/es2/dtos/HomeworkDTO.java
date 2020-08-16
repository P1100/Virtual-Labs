package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Homework;
import lombok.Data;

@Data
public class HomeworkDTO {
  private Homework.Status status = Homework.Status.NULL; //initial state
  private Boolean permanent;
  private String grade;
}
