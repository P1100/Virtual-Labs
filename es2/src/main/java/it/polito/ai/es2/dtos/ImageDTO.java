package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

@Data
public class ImageDTO extends RepresentationModel<ImageDTO> {
  private Long id;
  @PositiveOrZero
  private int revisionCycle;   // number of iteration for Implementation
  private Timestamp createDate; // LocalDateTime
  private Timestamp modifyDate;
}
