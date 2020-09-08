package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;

@Data
public class AssignmentDTO extends RepresentationModel<AssignmentDTO> {
  private Long id;
  private String name;
  @PastOrPresent
  private Timestamp releaseDate;
//  @FutureOrPresent
  private Timestamp expireDate;
}
