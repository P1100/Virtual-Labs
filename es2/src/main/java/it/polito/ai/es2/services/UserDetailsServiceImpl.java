package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.UserDTO;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findTopByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    org.springframework.security.core.userdetails.User UserDetailsImpl =
        new org.springframework.security.core.userdetails.User
            (user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                user.getAuthorities());
    return UserDetailsImpl;
  }
  
  public User save(UserDTO userDTO) {
    return saveUser(userDTO);
  }
  
  private User saveUser(UserDTO userDTO) {
    return saveUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getRoles());
  }
  
  /**
   * Main entrypoint for saving a User to the db
   */
  private User saveUser(String user, String pass, List<String> stringsRoles) {
    User newUser = new User();
    newUser.setUsername(user);
    newUser.setPassword(passwordEncoder.encode(pass));
    newUser.setRoles(UserDTO.getRolesFromStrings(stringsRoles));
    newUser.setEnabled(true);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);
    return userRepository.save(newUser);
  }
  
  /**
   * Used by UserController, main entrypoint for saving a User from REST API
   */
  public boolean addUser(String user, String pass, List<String> roles) {
    if (userRepository.findTopByUsername(user) == null) {
      saveUser(user, pass, roles);
      return true;
    }
    return false;
  }
  
  /**
   * Used by UserController
   */
  public boolean checkUser(String user, String pass) {
    if (user == null || pass == null)
      return false;
    User u = userRepository.findTopByUsername(user);
    return u != null && passwordEncoder.matches(pass, u.getPassword());
  }
}