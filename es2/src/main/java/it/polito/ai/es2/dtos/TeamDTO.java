package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;
import java.util.List;

@Data
public class TeamDTO extends RepresentationModel<TeamDTO> {
  private Long id;
  @NotBlank
  private String name;
  private boolean active;
  private boolean disabled;
  @PositiveOrZero
  private int maxVcpu,
      maxDisk,
      maxRam,
      maxRunningVM,
      maxTotVM; // sum of enabled and disabled
  @PositiveOrZero
  private long hoursTimeout;
  private Timestamp createdDate;
  @Transient
  private List<StudentDTO> students; // first student is the proposer
}