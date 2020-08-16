package it.polito.ai.es2.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity(name = "image")
//@MappedSuperclass
public class Image {
  @Id
  @GeneratedValue
  private String id;
  @CreationTimestamp
  @Column(name = "create_date")
  private Timestamp createDate; // LocalDateTime
  @UpdateTimestamp
  @Column(name = "modify_date")
  private Timestamp modifyDate;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] image;
  @OneToOne(mappedBy = "profilePhoto")
  private Student student;
  @OneToOne(mappedBy = "profilePhoto")
  private Professor professor;
  @OneToOne
  private Assignment assignment;
  @ManyToOne
  @JoinColumn
  private Homework homeworks;
  @OneToOne(mappedBy = "imageVm")
  private VM vm;
  //timestamp upload
  @CreatedBy
  protected Long createdBy;
  @CreatedDate
//  @Temporal(TemporalType.TIMESTAMP)
  protected Date ttcreatedDate;
  @LastModifiedBy
  protected Long ttlastModifiedBy;
  @LastModifiedDate
//  @Temporal(TemporalType.TIMESTAMP)
  protected Date ttlastModifiedDate;
  String test;
}
