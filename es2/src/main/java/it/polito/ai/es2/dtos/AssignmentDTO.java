package it.polito.ai.es2.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AssignmentDTO {
  private Timestamp releaseDate;
  private Timestamp expireDate;
}
