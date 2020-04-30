package ai.polito.es1.restsecurity;

import ai.polito.es1.restsecurity.security.jwt.JwtTokenProvider;
import ai.polito.es1.restsecurity.web.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
//@RequestMapping("/auth")
public class MyRestController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtTokenProvider jwtTokenProvider;
  
  @PostMapping("/signin")
  public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
    try {
      String username = data.getUsername();
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
      String token = jwtTokenProvider.createToken(username, Collections.singletonList("user"));
      Map<Object, Object> model = new HashMap<>();
      model.put("username", username);
      model.put("token", token);
      return ok(model);
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username/password supplied");
    }
  }
  
  @SuppressWarnings("rawtypes")
  @GetMapping("/me")
  public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
    Map<Object, Object> model = new HashMap<>();
    model.put("username", userDetails.getUsername());
    model.put("roles", userDetails.getAuthorities()
                           .stream()
                           .map(a -> a.getAuthority())
                           .collect(toList())
    );
    return ok(model);
  }
  
  @GetMapping("/esempio")
  public MyData home(Principal principal) {
    return MyData.builder()
               .name(principal != null ? principal.getName() : "<NO NAME>") //.name("Prova")x
               .date(new Date())
               .build();
  }
  
  @GetMapping("/r")
  public Date homerest() {
    return new Date();
  }
  
  @GetMapping("/r/{r1}")
//  @RequestMapping(value="/users/{id}",  method= {RequestMethod.GET, RequestMethod.POST})
  public String r1(@PathVariable String r1) {
    return (new Date()).toString() + "-" + r1;
  }
}
