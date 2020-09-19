package it.polito.ai.es2.dtos;

import it.polito.ai.es2.dtos.validators.MinMaxConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data
@MinMaxConstraint
public class CourseDTO extends RepresentationModel<CourseDTO> {
  @NotBlank
  private String id; // acronym
  @NotBlank
  private String fullName;
  @PositiveOrZero
  private int minSizeTeam;
  @Positive
  private int maxSizeTeam;
  private boolean enabled;
  private String vmModelPath;
}

