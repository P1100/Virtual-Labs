package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Transient;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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
  @Transient
  private Long teamId; // needed for vm creation
  @Transient
  private Long studentCreatorId; // needed for vm creation
  @Transient
  private StudentDTO creator;
  @Transient
  private List<StudentDTO> sharedOwners;
  @Transient
  private ImageDTO imageVm;
}
