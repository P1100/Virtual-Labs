package it.polito.ai.es2.dtos;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
public class ProfessorDTO extends RepresentationModel<ProfessorDTO> {
  @PositiveOrZero
  private Long id; // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  @Email
  @Pattern(regexp = "d[0-9]{1,9}@polito\\.it")
  private String email;
}
