package it.polito.ai.es2.dtos;

import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.util.List;

@Data
public class AssignmentDTO {
  private Long id;
  private String name;
  @PastOrPresent
  private Timestamp releaseDate;
//  @FutureOrPresent
  private Timestamp expireDate;
  @Transient
  private ImageDTO content;
  @Transient
  private List<ImplementationDTO> implementations;

}
