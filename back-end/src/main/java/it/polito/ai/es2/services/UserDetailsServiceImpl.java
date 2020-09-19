package it.polito.ai.es2.services;

import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findTopByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found: " + username);
    }
    org.springframework.security.core.userdetails.User UserDetailsImpl =
        new org.springframework.security.core.userdetails.User
            (user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                user.getAuthorities());
    return UserDetailsImpl;
  }
}