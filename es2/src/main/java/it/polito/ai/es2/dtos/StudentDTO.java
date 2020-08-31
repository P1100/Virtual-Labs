package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO extends RepresentationModel<StudentDTO> {
  @NotBlank
  private Long id;  // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @Email
  @Pattern(regexp = "s[0-9]{1,9}@studenti\\.polito\\.it")
  private String email;
  private Long imageId;
}

