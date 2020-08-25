package it.polito.ai.es2.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

@Data
@Entity(name = "image")
public class Image {
  @Id
  @GeneratedValue
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String type;
  @PositiveOrZero
  private int revisionCycle;   // number of iteration for Implementation
  @CreationTimestamp
  private Timestamp createDate; // LocalDateTime
  @UpdateTimestamp
  private Timestamp modifyDate;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] picByte;
  
  @OneToOne(mappedBy = "profilePhoto")
  private Student student;
  
  @OneToOne(mappedBy = "profilePhoto")
  private Professor professor;
  
  @OneToOne(mappedBy = "content")
  private Assignment assignment;
  
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn
  private Implementation submission;
  
  @OneToOne(mappedBy = "imageVm")
  private VM vm;
}
