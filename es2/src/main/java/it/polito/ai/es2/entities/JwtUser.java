package it.polito.ai.es2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
public class JwtUser {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private String username;
  @Column
  @JsonIgnore
  private String password;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private JwtRole role;
  
  @Transient
  private String token;
  
  public JwtUser(String username, String password, JwtRole role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
  public JwtUser() {
  }
}