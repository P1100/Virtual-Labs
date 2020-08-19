package it.polito.ai.es2.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

@Data
@Entity(name = "image")
public class Image {
  @Id
  @GeneratedValue
  private Long id;
  // number of iteration for Implementation
  @PositiveOrZero
  private int revisionCycle;
  @CreationTimestamp
  @Column(name = "create_date")
  private Timestamp createDate; // LocalDateTime
  @UpdateTimestamp
  @Column(name = "modify_date")
  private Timestamp modifyDate;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] data;
  
  @OneToOne(mappedBy = "profilePhoto")
  private Student avatarStudent;
  
  @OneToOne(mappedBy = "profilePhoto")
  private Professor avatarProfessor;
  
  @OneToOne(mappedBy = "content")
  private Assignment assignment;
  
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn
  private Implementation submission;
  
  @OneToOne(mappedBy = "imageVm")
  private VM vm;
}
