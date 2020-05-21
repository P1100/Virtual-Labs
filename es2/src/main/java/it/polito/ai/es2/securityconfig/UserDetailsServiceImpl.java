package it.polito.ai.es2.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
  
  private User saveUser(String user, String pass, List<String> stringsRoles) {
    User newUser = new User();
    newUser.setUsername(user);
    newUser.setPassword(passwordEncoder.encode(pass));
    newUser.setRoles(UserDTO.getRolesConverted(stringsRoles));
    newUser.setEnabled(true);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);
    return userRepository.save(newUser);
  }
  
  /**
   * Used by UserController
   */
  public boolean addUser(String user, String pass, String role) {
//    User u = new User("admin2", bcryptEncoder.encode("adminpassword"), Role.ADMIN);
    if (userRepository.findTopByUsername(user) == null) {
      saveUser(user, pass, Collections.singletonList("ROLE_" + role.toUpperCase()));
      return true;
    }
    return false;
  }
  
  /**
   * Used by UserController
   */
  public boolean checkUser(String user, String pass) {
    User u = userRepository.findTopByUsername(user);
    return passwordEncoder.matches(pass, u.getPassword());
  }
}