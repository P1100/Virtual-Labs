package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Implementation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Transient;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ImplementationDTO {
  private Long id;
  private Implementation.Status status;
  private Boolean permanent;
  @Length(max = 3)
  private String grade;
  @PastOrPresent
  private Timestamp readStatus, definitiveStatus;
  private Timestamp lastStatus;
  private String currentCorrection;
  @Transient
  private List<ImageDTO> imageSubmissions;
  @Transient
  private StudentDTO student;
}
