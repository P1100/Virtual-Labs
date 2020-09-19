package it.polito.ai.es2.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "image")
public class Image {
  @Id
  @GeneratedValue
  private Long id;
  @NotBlank
  @EqualsAndHashCode.Include
  private String name;
  @NotBlank
  @EqualsAndHashCode.Include
  private String type;
  @PositiveOrZero
  private int revisionCycle;   // number of iteration for Implementation
  @CreationTimestamp
  private Timestamp createDate; // LocalDateTime
  @UpdateTimestamp
  private Timestamp modifyDate;
  private String directLink;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] picBytes;

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
