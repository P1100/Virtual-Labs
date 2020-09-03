package it.polito.ai.es2.controllers;

import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import it.polito.ai.es2.services.interfaces.UserStudProfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("/notification")
@Validated
public class Notification_Controller {
  @Autowired
  NotificationService notificationService;
  @Autowired
  TeamService teamService;
  @Autowired
  UserStudProfService userStudProfService;

  @GetMapping("/user/confirm/{token}")
  public String confirmUser(@PathVariable @NotBlank String token) {
    boolean confirm = userStudProfService.confirmUser(token);
    if (confirm) {
      return "token_confirmed";
    } else
      return "token_error";
  }

  @GetMapping("/team/confirm/{token}")
  public String confirmTokenTeam(@PathVariable @NotBlank String token) {
    boolean confirm = teamService.confirmTeam(token);
    if (confirm)
      return "token_confirmed";
    else
      return "token_error";
  }

  @GetMapping("/team/reject/{token}")
  public String rejectTokenTeam(@PathVariable @NotBlank String token) {
    boolean reject = teamService.rejectTeam(token);
    if (reject)
      return "token_rejcted";
    else
      return "token_error";
  }
}
