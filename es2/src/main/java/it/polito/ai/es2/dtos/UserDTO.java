package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Role;
import it.polito.ai.es2.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  @NotBlank
  private String username;
  @NotBlank
  @Size(min = 6, max = 30)
  private String password;
  private List<String> roles = new ArrayList<>();
  private User.TypeUser typeUser;

  @Transient
  private Long id;  // alias of username
  @Transient
  @NotBlank
  private String firstName;
  @Transient
  @NotBlank
  private String lastName;
  @Transient
  @Email
  @Pattern(regexp = "[sd][0-9]{1,9}@(studenti\\.)?polito\\.it")
  private String email;
  /**
   * Used for convenient mapping in userDTOs, studentDTOs, and professorDTOs. Don't remove!
   */
  @Transient
  private Long imageId;

  /**
   * @param strings eg: {"AdMIN", "UsER"}
   * @return {Role: "AdMIN", "UsER"}
   */
  public static List<Role> convertStringsToRoles(List<String> strings) {
    List<Role> roles = new ArrayList<>();
    for (String role : strings) {
      roles.add(new Role("ROLE_" + role.toUpperCase()));
    }
    return roles;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (String role : roles) {
      authorities.add(new SimpleGrantedAuthority(role));
    }
    return authorities;
  }
}