package it.polito.ai.es2.controllers;

import it.polito.ai.es2.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notification")
public class Notification_Controller {
  @Autowired
  NotificationService notificationService;
  
  @GetMapping("/confirm/{token}")
  public String confirm_token(@PathVariable String token) {
    boolean confirm = notificationService.confirm(token);
    if (confirm)
      return "confirmed_token";
    else
      return "error_template";
  }
  
  @GetMapping("/reject/{token}")
  public String reject_token(@PathVariable String token) {
    boolean reject = notificationService.reject(token);
    if (reject)
      return "rejcted_token";
    else
      return "error_template";
  }
}
