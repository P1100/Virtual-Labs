package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
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
