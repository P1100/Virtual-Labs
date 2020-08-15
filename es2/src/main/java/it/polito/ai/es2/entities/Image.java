package it.polito.ai.es2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "image")
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
}
