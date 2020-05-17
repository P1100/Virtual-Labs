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
  
  @GetMapping("/notification/confirm/{token}")
  public void confirm_token(@PathVariable String token) {
  }
  
  @GetMapping("/notification/reject/{token}")
  public void reject_token(@PathVariable String token) {
  }
  
  @PostMapping("/propose")
  public String propose_team(@ModelAttribute("command") TeamViewModel teamViewModel,
                             BindingResult bindingResult, Model model) {
    // TODO: remove testing parameter later
    teamViewModel = new TeamViewModel((long) 999, "Team_testing_email", "Course_testing_email", Arrays.asList("S1", "S2", "S3"));
    TeamDTO created_team;
    try {
      created_team = teamService.proposeTeam(teamViewModel.getCourseId(), teamViewModel.getName(), teamViewModel.getMemberIds());
    } catch (Exception e) {
      e.printStackTrace();
      return "error_template";
    }
    if (created_team == null)
      return "error_template";
    notificationService.notifyTeam(created_team, teamViewModel.getMemberIds());
    return "home_csv";
  }
//      sb.append("\n\nLink to accept token:\n" + url + "/notification/confirm/"+temp.getId());
//      sb.append("\n\nLink to remove token:\n" + url + "/notification/reject/"+temp.getId());
}
