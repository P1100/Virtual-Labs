package it.polito.ai.es2.securityconfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * E' la stessa tabella che usa jdbcAuthentication (non perfettamente uguale, ma compatibile)
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
  //  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
  @Id
  private String username;
  @Column
  @JsonIgnore
  private String password;
  //  String email;
  boolean enabled;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "authorities_global", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "authority", referencedColumnName = "name"))
  private List<Role> roles = new ArrayList<>();
  @Transient
  private String token;
  
  public User(String username, String password, List<Role> roles) {
    this.username = username;
    this.password = password;
//    this.roles = roles;
  }
  
  public User(String name) {
  }
}