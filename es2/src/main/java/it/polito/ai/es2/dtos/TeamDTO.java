package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Data // Note that it doesn't work with modelMapper!
public class TeamDTO extends RepresentationModel<CourseDTO> {
  private Long id;
  private String name;
  private int status = 0; //0 inactive, 1 active
  private int
      maxVcpu,
      maxDisk,
      maxRam,
      maxRunningVM,
      maxTotVM; // sum of enabled and disabled
}