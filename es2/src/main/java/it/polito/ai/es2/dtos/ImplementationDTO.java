package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Implementation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImplementationDTO extends RepresentationModel<ImplementationDTO> {
  private Long id;
  private Implementation.Status status;
  private Boolean permanent;
  @Length(max = 3)
  private String grade;
  @PastOrPresent
  private Timestamp read_status, definitive_status;
}
