package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.ProfessorDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.UserDTO;
import it.polito.ai.es2.securityconfig.jwt.JwtRequest;
import it.polito.ai.es2.securityconfig.jwt.JwtResponse;
import it.polito.ai.es2.securityconfig.jwt.JwtTokenUtil;
import it.polito.ai.es2.services.interfaces.UserStudProfService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin
@PreAuthorize("permitAll()") // doesnt override httpsecurity settings!
public class APIUsers_RestController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Qualifier("userDetailsServiceImpl")
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private UserStudProfService userStudProfService;

  // {"username":"admin","password":"a"}
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
    try {
      // Using UserDetailsServiceImpl (WebConfig).
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
    UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String token = jwtTokenUtil.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token));
  }

  //{"username":"1354623","password":"passss","firstName":"fi","lastName":"la","email":"s111111@studenti.polito.it", "roles":["ADMIN", "user"]}
  @PostMapping("/student")
  public UserDTO registerStudent(@Valid @RequestBody UserDTO userDTO) {
    System.out.println(userDTO);
    userDTO.setRoles(Arrays.asList("STUDENT"));
    StudentDTO studentDTO = modelMapper.map(userDTO, StudentDTO.class);
    studentDTO.setId(Long.valueOf(userDTO.getUsername()));
    System.out.println(studentDTO);
    System.out.println(userStudProfService.addNewUser(userDTO));
    System.out.println(userStudProfService.addStudent(studentDTO));
    return userDTO;
  }

  // {"username":"345323445","password":"passss","firstName":"prof","lastName":"la","email":"d111111@polito.it", "roles":["ADMIN", "user"]}
  @PostMapping("/professor")
  public UserDTO registerProfessor(@Valid @RequestBody UserDTO userDTO) {
    System.out.println(userDTO);
    userDTO.setRoles(Arrays.asList("PROFESSOR"));
    ProfessorDTO professorDTO = modelMapper.map(userDTO, ProfessorDTO.class);
    professorDTO.setId(Long.valueOf(userDTO.getUsername()));
    System.out.println(professorDTO);
    System.out.println(userStudProfService.addNewUser(userDTO));
    System.out.println(userStudProfService.addProfessor(professorDTO));
    return userDTO;
  }
}