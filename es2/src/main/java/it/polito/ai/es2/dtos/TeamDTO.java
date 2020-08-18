package it.polito.ai.es2.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder // Note that it doesn't work with modelMapper!
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