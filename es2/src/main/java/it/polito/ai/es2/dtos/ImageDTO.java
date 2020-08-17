package it.polito.ai.es2.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ImageDTO {
  private Long id;
  private int revisionCycle;
  private Timestamp createDate;
  private Timestamp modifyDate;
  private byte[] data;
}
