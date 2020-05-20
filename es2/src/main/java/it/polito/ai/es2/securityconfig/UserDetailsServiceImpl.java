package it.polito.ai.es2.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        new ArrayList<>());
  }
  
  public User save(UserDetailsImpl user) {
    User newUser = new User();
    newUser.setUsername(user.getUsername());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(newUser);
  }
  
  /*  @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
      for (Role role : user.getRoles()) {
        authorities.add(new SimpleGrantedAuthority(role.getName()));
      }
      return authorities;
    }*/
  public boolean addUser(String user, String pass, String role) {
//    User u = new User("admin2", bcryptEncoder.encode("adminpassword"), Role.ADMIN);
    if (userRepository.findTopByUsername(user) == null) {
      User newUser = new User();
      newUser.setUsername(user);
      newUser.setPassword(passwordEncoder.encode(pass));
      userRepository.save(newUser);
      return true;
    }
    return false;
  }
  
  public boolean checkUser(String user, String pass) {
    User u = userRepository.findTopByUsername(user);
    return passwordEncoder.matches(pass, u.getPassword());
  }
}