package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/API/users")
public class APIUsers_RestController {
  @Autowired
  private ModelHelper modelHelper;

  @PostMapping("/professor")
  public void registerProfessor(UserDTO userDTO) {
    System.out.println(userDTO);
  }

  @PostMapping("/student")
  public void registerStudent(UserDTO userDTO) {
    System.out.println(userDTO);
  }
}
/*

@RequestMapping("/users")
//@PreAuthorize("hasRole('ADMIN')")
public class Users_Controller {
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
  */
