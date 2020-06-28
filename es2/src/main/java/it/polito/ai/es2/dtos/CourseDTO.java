package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDTO extends RepresentationModel<CourseDTO> {
  private String id;
  private int min;
  private int max;
  private boolean enabled;
  private String professor;
}

