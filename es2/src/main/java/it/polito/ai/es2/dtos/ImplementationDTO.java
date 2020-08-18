package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Implementation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImplementationDTO {
  private Long id;
  private Implementation.Status status = Implementation.Status.NULL; // initial state
  private Boolean permanent = false;
  private String grade;
}
