package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

@Data
public class ImageDTO extends RepresentationModel<ImageDTO> {
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String type;
  @PositiveOrZero
  private int revisionCycle;   // number of iteration for Implementation
  private Timestamp createDate; // LocalDateTime
  private Timestamp modifyDate;
  private String directLink;
  @Transient
  private String imageStringBase64;
}
