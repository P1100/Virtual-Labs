package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data // Note that it doesn't work with modelMapper!
public class TeamDTO extends RepresentationModel<TeamDTO> {
  private Long id;
  @NotBlank
  private String name;
  @Range(min = 0, max = 1)
  private int status = 0; //0 inactive, 1 active
  @PositiveOrZero
  private int
      maxVcpu,
      maxDisk,
      maxRam,
      maxRunningVM,
      maxTotVM; // sum of enabled and disabled
}