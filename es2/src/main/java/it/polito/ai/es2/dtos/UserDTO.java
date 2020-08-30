package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Role;
import it.polito.ai.es2.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements UserDetails {

  @NotBlank
  private String username;
  @NotBlank
  private String password;
  private List<String> roles;

  @NotNull
  private Long id;  // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @Email
  @Pattern(regexp = "s[0-9]{1,9}@(studenti\\.)?polito\\.it")
  private String email;

  public List<String> getRoles() {
    if (roles == null)
      return new ArrayList<>();
    return roles.stream().collect(Collectors.toList());
  }

  public UserDTO(User user) {
    username = user.getUsername();
    roles = user.getRolesStringsList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  public static List<Role> getRolesFromStrings(List<String> strings) {
    List<Role> roles = new ArrayList<>();
    for (String role : strings) {
      roles.add(new Role(role.toUpperCase()));
    }
    return roles;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (String role : roles) {
      authorities.add(new SimpleGrantedAuthority(role));
    }
    return authorities;
  }
}