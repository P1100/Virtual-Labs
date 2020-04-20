package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

//@Builder --> org.modelmapper.MappingException: ModelMapper mapping errors: 1) Failed to instantiate instance of destination it.polito.ai.es2.dtos.CourseDTO. Ensure that it.polito.ai.es2.dtos.CourseDTO has a non-private no-argument constructor.
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO {
    @Id
    private String id;
    private String name;
    private String firstName;
}
