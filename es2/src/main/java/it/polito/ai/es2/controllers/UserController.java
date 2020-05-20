package it.polito.ai.es2.controllers;

import it.polito.ai.es2.securityconfig.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class UserController {
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;
  
  @GetMapping("/add/{user}/{pass}/{role}")
  @ResponseBody
  public boolean adduser(@PathVariable String user, @PathVariable String pass, @PathVariable String role) {
    String r;
    try {
      r = role;
    } catch (IllegalArgumentException e) {
      r = "user";
    }
    return userDetailsServiceImpl.addUser(user, pass, r);
  }
  
  @GetMapping("/{user}/{pass}")
  @ResponseBody
  public boolean matchpass(@PathVariable String user, @PathVariable String pass) {
    return userDetailsServiceImpl.checkUser(user, pass);
  }
}
