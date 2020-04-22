package it.polito.ai.es2.domains;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class StudentViewModel {
  @CsvBindByName
  private String id;
  @CsvBindByName
  private String name;
  @CsvBindByName
  private String firstName;
}

