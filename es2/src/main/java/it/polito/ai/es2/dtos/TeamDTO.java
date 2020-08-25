package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class TeamDTO extends RepresentationModel<TeamDTO> {
  private Long id;
  @NotBlank
  private String name;
  private boolean active;
  @PositiveOrZero
  private int maxVcpu,
      maxDisk,
      maxRam,
      maxRunningVM,
      maxTotVM; // sum of enabled and disabled
}