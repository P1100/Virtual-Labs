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
  private String fullName;
  private int minEnrolled;
  private int maxEnrolled;
  private boolean enabled;
}

