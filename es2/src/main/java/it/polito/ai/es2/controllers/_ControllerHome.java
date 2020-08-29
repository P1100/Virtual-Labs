package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@PreAuthorize("permitAll()") // doesnt override httpsecurity settings!
public class _ControllerHome {
  @Autowired
  ModelHelper urlSettings;
  @Autowired
  UserRepository userRepository;

  @RequestMapping(value = "/API", produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String getAPI() {
    String baseUrl = urlSettings.getBaseUrl();
    String json = "{" +
                      "\"Courses\":\"" + baseUrl + "/API/courses\","
                      + "\"Students\":\"" + baseUrl + "/API/students\","
                      + "\"Teams\":\"" + baseUrl + "/API/teams\","
                      + "\"Images\":\"" + baseUrl + "/API/images\""
                      + "}";
    return json;
  }

  /**
   * Test code
   */
  @GetMapping("/")
  @ResponseBody
  public String getPrincipal(Principal principal, @AuthenticationPrincipal User spring_security_user) {
    if (principal == null)
      return "no principal";
    return "Principal got from HTTP HttpServletRequest:<br>" + principal.getName() +
               "<br><br>SecurityContextHolder.getContext().getAuthentication() object:<br>" + SecurityContextHolder.getContext().getAuthentication()
               + "<br><br>@AuthenticationPrincipal User:<br>" + spring_security_user
               + "<br><br>UderDetails from userRepository.findTopByUsername(principal.getName())/db:<br>" + userRepository.findTopByUsername(principal.getName())
        ;
  }

}
