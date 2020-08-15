package it.polito.ai.es2.dtos;

import lombok.Data;

@Data
public class VmDTO {
  private Long id;
  private int vcpu;
  private int diskSpace;
  private int ram;
  private boolean active;
}
