package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO extends RepresentationModel<CourseDTO> {
  private Long id;
  private String lastName;
  private String firstName;
  private String email;
}

