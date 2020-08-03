package it.polito.ai.es2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
// [ SQL quoted identifiers] "User" is a reserved word in some SQL implementations, so we escape the table name
@Table(name = "\"user\"")
public class User {
  @Id
  private String username;
  @Column
  @JsonIgnore
  private String password;
  boolean enabled;
  boolean accountNonExpired;
  boolean credentialsNonExpired;
  boolean accountNonLocked;
  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "authority", referencedColumnName = "name"))
  private List<Role> roles = new ArrayList<>();
  @Transient
  private String token;
  
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return authorities;
  }
  
  public List<String> getRolesStringsList() {
    List<String> stringRoles = new ArrayList<>();
    for (Role role : roles) {
      stringRoles.add(role.getName());
    }
    return stringRoles;
  }
}