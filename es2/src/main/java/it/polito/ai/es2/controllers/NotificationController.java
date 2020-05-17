package it.polito.ai.es2.controllers;

import it.polito.ai.es2.domains.TeamViewModel;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.NotificationService;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/notification")
public class NotificationController {
  @Autowired
  TeamService teamService;
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
  
  @PostMapping("/propose")
  public String propose_team(@ModelAttribute("command") TeamViewModel teamViewModel,
                             BindingResult bindingResult, Model model) {
    // TODO: remove testing parameter later
//    teamViewModel = new TeamViewModel((long) 999, "Team_testing_email", "Course_testing_email", Arrays.asList("S1", "S2", "S3"));
    System.out.println("TeamViewModel:" + teamViewModel);
    TeamDTO created_team;
    teamViewModel.getMemberIds().removeAll(Arrays.asList("", null));
    try {
      created_team = teamService.proposeTeam(teamViewModel.getCourseId(), teamViewModel.getName(), teamViewModel.getMemberIds());
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("error", e.getMessage());
      return "error_template";
    }
    if (created_team == null)
      return "error_template";
    return "home_csv";
  }
}
