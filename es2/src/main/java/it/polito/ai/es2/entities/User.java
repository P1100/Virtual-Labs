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
  /**
   * Used only internally by the back end
   */
  public enum TypeUser {STUDENT, PROFESSOR}

  /**
   * Username must be equal to student/professor id !!!
   */
  @Id
  private String username;
  @NotBlank
  private String password;

  private boolean enabled = false;
  private boolean accountNonExpired = false;
  private boolean credentialsNonExpired = false;
  private boolean accountNonLocked = true;

  private TypeUser typeUser;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private Student student;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private Professor professor;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "authority", referencedColumnName = "name"))
  private List<Role> roles = new ArrayList<>();

  @OneToOne(mappedBy = "user")
  private Token tokenSignup;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
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

  public void addSetStudent(Student x) {
    if (student != null)
      throw new RuntimeException("JPA-User: overriding a OneToOne or ManyToOne field might be an error");
    student = x;
    x.setUser(this);
  }

  public void addSetProfessor(Professor x) {
    if (professor != null)
      throw new RuntimeException("JPA-User: overriding a OneToOne or ManyToOne field might be an error");
    professor = x;
    x.setUser(this);
  }
}