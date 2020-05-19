package it.polito.ai.es2.controllers;

import it.polito.ai.es2._provecodicelearning.MyTestingService;
import it.polito.ai.es2.entities.JwtRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class _LearningAndTestingController {
  @Autowired
  MyTestingService myTestingService;
  
  @GetMapping("/testmap")
  @ResponseBody
  public Map<String, String> testMapBinding() {
    Map<String, String> m = new HashMap<String, String>(); //Map.of
    m.put("key1", "value1");
    m.put("key2", "value2");
    return m;
  }
  @GetMapping("/testing/adduser/{user}/{pass}/{role}")
  @ResponseBody
  public boolean adduser(@PathVariable String user, @PathVariable String pass, @PathVariable String role) {
    JwtRole r;
    try {
      r = JwtRole.valueOf(role);
    } catch (IllegalArgumentException e) {
      r = JwtRole.USER;
    }
    return myTestingService.addUser(user,pass,r);
  }
  @GetMapping("/testing/{user}/{pass}")
  @ResponseBody
  public boolean matchpass(@PathVariable String user, @PathVariable String pass) {
    return myTestingService.checkUser(user,pass);
  }
  @GetMapping("/principal")
  @ResponseBody
  public String getPrincipal(Principal principal) {
    if (principal == null)
      return "no principal";
    return principal.getName();
  }
  
  @RequestMapping(value = {"/hello"}, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String hello() {
    return "{var:'Hello'; var2:'World1'}";
  }
  
  @GetMapping("/hello2")
  @ResponseBody //default: text/plain
  public String hello2() {
    return "Hello World 2";
  }
}
