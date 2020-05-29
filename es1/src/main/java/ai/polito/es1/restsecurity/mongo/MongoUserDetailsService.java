package ai.polito.es1.restsecurity.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

//@Component
public class MongoUserDetailsService implements UserDetailsService {
  @Autowired
  private MongoUsersRepository repository;
  @Autowired
  PasswordEncoder pe;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    repository.save(new Users(null,"user0", "pass"));
//    repository.save(new Users(null,"user1", pe.encode("pass")));  ----> NOT WORKING
    
    Users user = repository.findByUsername(username);
    System.out.println("loadUserByUsername(String username):" + user.toString() + " - findByUsername->" + username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
    return new User(user.getUsername(), pe.encode(user.getPassword()), authorities);
  }
}