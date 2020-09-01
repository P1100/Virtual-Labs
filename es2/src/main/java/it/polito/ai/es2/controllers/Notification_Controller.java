package it.polito.ai.es2.controllers;

import it.polito.ai.es2.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("/notification")
@Validated
@PreAuthorize("permitAll()")
public class Notification_Controller {
  @Autowired
  NotificationService notificationService;

  @GetMapping("/user/confirm/{token}")
  public String confirmUser(@PathVariable @NotBlank String token) {
    boolean confirm = notificationService.confirmUser(token);
    if (confirm) {
      return "token_confirmed";
    } else
      return "token_error";
  }

  @GetMapping("/team/confirm/{token}")
  public String confirmTokenTeam(@PathVariable @NotBlank String token) {
    boolean confirm = notificationService.confirmTeam(token);
    if (confirm)
      return "token_confirmed";
    else
      return "token_error";
  }

  @GetMapping("/team/reject/{token}")
  public String rejectTokenTeam(@PathVariable @NotBlank String token) {
    boolean reject = notificationService.rejectTeam(token);
    if (reject)
      return "token_rejcted";
    else
      return "token_error";
  }
}
