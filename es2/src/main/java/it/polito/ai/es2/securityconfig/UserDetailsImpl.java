package it.polito.ai.es2.securityconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
  private String username;
  private String password;
  
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
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }
}