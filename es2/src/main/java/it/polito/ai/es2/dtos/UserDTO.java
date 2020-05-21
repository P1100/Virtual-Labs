package it.polito.ai.es2.dtos;

import it.polito.ai.es2.entities.Role;
import it.polito.ai.es2.entities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO implements UserDetails {
  private String username;
  private String password;
  private List<String> roles;
  
  public List<String> getRoles() {
    return roles.stream().collect(Collectors.toList());
  }
  
  public UserDTO(User user) {
    this.username = user.getUsername();
    this.roles = user.getRolesStringsList();
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