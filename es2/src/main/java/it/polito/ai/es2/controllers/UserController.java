package it.polito.ai.es2.controllers;

import it.polito.ai.es2.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;
  
  // GET http://localhost:8080/users/add/user/pass/"ADMIN","USER"
  @GetMapping("/add/{user}/{pass}/{roles}")
  @ResponseBody
  public boolean adduser(@PathVariable String user, @PathVariable String pass, @PathVariable String[] roles) {
    List<String> stringList = new ArrayList<>();
    for (String role : roles) {
      if (role != null)
        stringList.add("ROLE_" + role.toUpperCase());
    }
    if (stringList.size() == 0)
      stringList = Collections.singletonList("ROLE_GUEST");
    return userDetailsServiceImpl.addUser(user, pass, stringList);
  }
  
  @GetMapping("/check/{user}/{pass}")
  @ResponseBody
  public boolean matchpass(@PathVariable String user, @PathVariable String pass) {
    return userDetailsServiceImpl.checkUser(user, pass);
  }
}
