package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO extends RepresentationModel<CourseDTO> {
  private String id;
  private String name;
  private String firstName;
}
