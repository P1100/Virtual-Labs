package it.polito.ai.es2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// "User" is a reserved word in some SQL implementations, so we escape the table name [SQL quoted identifiers]
@Table(name = "\"user\"")
public class User {
  @Id
  private String username;
  @NotBlank
  private String password;

  boolean enabled = false;
  boolean accountNonExpired = false;
  boolean credentialsNonExpired = false;
  boolean accountNonLocked = true;

  //  @Size(max = 20)
//  @Column(length = 20)
//  @JsonIgnore
  private String activationToken;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "authority", referencedColumnName = "name"))
  private List<Role> roles = new ArrayList<>();

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return authorities;
  }

  public List<String> convertStringsToRoles() {
    List<String> stringRoles = new ArrayList<>();
    for (Role role : roles) {
      stringRoles.add(role.getName());
    }
    return stringRoles;
  }
}