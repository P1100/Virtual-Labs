package it.polito.ai.es2.controllers;

import it.polito.ai.es2.securityconfig.UserDetailsImpl;
import it.polito.ai.es2.securityconfig.UserDetailsServiceImpl;
import it.polito.ai.es2.securityconfig.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
@RequestMapping("/jwt")
@CrossOrigin
public class JwtRestController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  
  // {"username":"jack","password":"pass"}
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    final UserDetails userDetails = userDetailsService
                                        .loadUserByUsername(authenticationRequest.getUsername());
    final String token = jwtTokenUtil.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token));
  }
  
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<?> saveUser(@RequestBody UserDetailsImpl user) throws Exception {
    return ResponseEntity.ok(userDetailsService.save(user));
  }
  
  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}

class JwtRequest implements Serializable {
  private static final long serialVersionUID = 5926468583005150707L;
  private String username;
  private String password;
  
  //need default constructor for JSON Parsing
  public JwtRequest() {
  }
  
  public JwtRequest(String username, String password) {
    this.setUsername(username);
    this.setPassword(password);
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
}

class JwtResponse implements Serializable {
  private static final long serialVersionUID = -8091879091924046844L;
  private final String jwttoken;
  
  public JwtResponse(String jwttoken) {
    this.jwttoken = jwttoken;
  }
  
  public String getToken() {
    return this.jwttoken;
  }
}