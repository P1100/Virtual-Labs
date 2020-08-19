package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageDTO extends RepresentationModel<ImageDTO> {
  private Long id;
  private int revisionCycle;
  private Timestamp createDate;
  private Timestamp modifyDate;
}
