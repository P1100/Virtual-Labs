package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignmentDTO {
  private Long id;
  private String name;
  private Timestamp releaseDate;
  private Timestamp expireDate;
}
