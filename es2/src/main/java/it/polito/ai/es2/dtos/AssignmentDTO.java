package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignmentDTO extends RepresentationModel<AssignmentDTO> {
  private Long id;
  private String name;
  @PastOrPresent
  private Timestamp releaseDate;
  @FutureOrPresent
  private Timestamp expireDate;
}
