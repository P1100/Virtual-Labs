package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.UserDTO;
import it.polito.ai.es2.securityconfig.jwt.JwtRequest;
import it.polito.ai.es2.securityconfig.jwt.JwtResponse;
import it.polito.ai.es2.securityconfig.jwt.JwtTokenUtil;
import it.polito.ai.es2.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
//@CrossOrigin
@PreAuthorize("permitAll()") // doesnt override httpsecurity settings!
public class Jwt_RestController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @GetMapping("/testjwt")
  public String test() {
    return "THIS IS A OK TEST";
  }

  @PostMapping("/testjwt")
  public String test2() {
    return "THIS IS A OK TEST";
  }

  // {"username":"admin","password":"a"}
//  {
//    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5MzAzODM4MywiaWF0IjoxNTkzMDIwMzgzfQ.bQUxdDKJWPVOKM2g1LnR5ubFjs2GNhQutsh43ARNTmQ_44G2ZbHyxfcchY8DmoxqSwNpU6jfusewmWuKkMJAUQ"
//  }
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    UserDetails userDetails = userDetailsService
                                  .loadUserByUsername(authenticationRequest.getUsername());
    String token = jwtTokenUtil.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token));
  }

  // {"username":"admin","password":"a"}
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
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

