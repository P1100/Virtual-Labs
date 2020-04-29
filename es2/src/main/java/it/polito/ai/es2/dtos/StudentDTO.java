package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO: check DTO validation https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation
 */
//@Builder --> org.modelmapper.MappingException: ModelMapper mapping errors: 1) Failed to instantiate instance of destination it.polito.ai.es2.dtos.CourseDTO. Ensure that it.polito.ai.es2.dtos.CourseDTO has a non-private no-argument constructor.
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO {
  private String id;
  private String name;
  private String firstName;
}
