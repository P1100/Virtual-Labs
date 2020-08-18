package it.polito.ai.es2.domains;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StudentViewModel {
  @CsvBindByName
  @NotBlank
  private Long id;
  @CsvBindByName
  @NotBlank
  private String name;
  @CsvBindByName
  @NotBlank
  private String firstName;
}

