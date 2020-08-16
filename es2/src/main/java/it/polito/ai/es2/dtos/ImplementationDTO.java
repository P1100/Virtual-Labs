package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Implementation;
import lombok.Data;

@Data
public class ImplementationDTO {
  private Implementation.Status status = Implementation.Status.NULL; //initial state
  private Boolean permanent;
  private String grade;
}
