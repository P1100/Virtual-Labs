package it.polito.ai.es2.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Builder
public class TeamDTO extends RepresentationModel<CourseDTO> {
  private Long id;
  private String name;
  private int status;
  // TODO: check again with entities, not sure
  private MultipartFile modelVM;
  private Long maxVcpu;
  private Long maxDiskSpace;
  private Long maxRam;
  private Long maxVmIstancesEnabled;
  private Long maxVmIstancesAvailable;
}