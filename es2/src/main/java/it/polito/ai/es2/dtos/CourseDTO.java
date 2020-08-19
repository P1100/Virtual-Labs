package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDTO extends RepresentationModel<CourseDTO> {
  @Id
  @NotBlank
  private String id; // acronym
  @NotBlank
  private String fullName;
  @PositiveOrZero
  private int minSizeGroup;
  @Positive
  private int maxSizeGroup;
  @NotNull
  private boolean enabled;
  private String vmModelPath;
}

