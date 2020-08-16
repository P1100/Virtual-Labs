package it.polito.ai.es2.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity(name = "image")
//@MappedSuperclass
public class Image {
  @Id
  @GeneratedValue
  private Long id;
  
  // number of iterations for Implementation
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
  private Student student;
  
  @OneToOne(mappedBy = "profilePhoto")
  private Professor professor;
  
  @OneToOne(mappedBy = "content")
  private Assignment assignment;
  
  @ManyToOne
  @JoinColumn
  private Implementation submission;
  
  @OneToOne(mappedBy = "imageVm")
  private VM vm;
}
