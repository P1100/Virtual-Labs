package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Implementation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;

@Data
public class ImplementationDTO extends RepresentationModel<ImplementationDTO> {
  private Long id;
  private Implementation.Status status;
  private Boolean permanent;
  @Length(max = 3)
  private String grade;
  @PastOrPresent
  private Timestamp readStatus, definitiveStatus;
}
