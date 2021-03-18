package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.UserDTO;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.securityconfig.jwt.JwtRequest;
import it.polito.ai.es2.securityconfig.jwt.JwtResponse;
import it.polito.ai.es2.securityconfig.jwt.JwtTokenUtil;
import it.polito.ai.es2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class APIUsers_RestController {
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Qualifier("userDetailsServiceImpl")
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private UserService userService;

  // {"username":"admin","password":"a"}
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
    try { // Using UserDetailsServiceImpl (WebConfig)
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
    UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String token = jwtTokenUtil.generateToken(userDetails);
    JwtResponse jwtResponse = new JwtResponse(token);
    jwtResponse.setRole(userDetails.getAuthorities().toArray()[0].toString().toLowerCase().replace("role_", ""));
    return ResponseEntity.ok(jwtResponse);
  }

  //{"username":"1354623","password":"passss","firstName":"fi","lastName":"la","email":"s111111@studenti.polito.it", "roles":["ADMIN", "STUDENT"]}
  @PostMapping("/student")
  public UserDTO registerStudent(@Valid @RequestBody UserDTO userDTO) {
    System.out.println(userDTO);
    userDTO.setRoles(Collections.singletonList("STUDENT"));
    userDTO.setTypeUser(User.TypeUser.STUDENT);
    userService.addNewUser(userDTO);
    return userDTO;
  }

  // {"username":"345323445","password":"passss","firstName":"prof","lastName":"la","email":"d111111@polito.it", "roles":["ADMIN", "PROFESSOR"]}
  @PostMapping("/professor")
  public UserDTO registerProfessor(@Valid @RequestBody UserDTO userDTO) {
    userDTO.setRoles(Arrays.asList("PROFESSOR"));
    userDTO.setTypeUser(User.TypeUser.PROFESSOR);
    userService.addNewUser(userDTO);
    return userDTO;
  }
}