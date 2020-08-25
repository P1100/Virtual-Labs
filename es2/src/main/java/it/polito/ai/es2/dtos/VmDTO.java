package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PositiveOrZero;

@Data
public class VmDTO extends RepresentationModel<VmDTO> {
  private Long id;
  @PositiveOrZero
  private int vcpu;
  @PositiveOrZero
  private int disk;
  @PositiveOrZero
  private int ram;
  private boolean active;
}
