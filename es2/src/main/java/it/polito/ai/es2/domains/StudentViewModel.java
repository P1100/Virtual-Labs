package it.polito.ai.es2.domains;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

// TODO: delete later, use DTO
@Data
public class StudentViewModel {
  @CsvBindByName
  @NotNull
  private Long id;
  @CsvBindByName
  @NotBlank
  private String firstName;
  @CsvBindByName
  @NotBlank
  private String lastName;
  @CsvBindByName
  @NotBlank
  @Email
  @Pattern(regexp = "s[0-9]{1,9}@studenti\\.polito\\.it")
  private String email;
}

