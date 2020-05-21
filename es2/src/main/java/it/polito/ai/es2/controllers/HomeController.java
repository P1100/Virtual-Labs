package it.polito.ai.es2.controllers;

import it.polito.ai.es2.domains.TeamViewModel;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.securityconfig.UserDetailsServiceImpl;
import it.polito.ai.es2.securityconfig.UserRepository;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Arrays;

@Controller
public class HomeController {
  @Autowired
  TeamService teamService;
  @Autowired
  UserDetailsServiceImpl userDetailsService;
  @Autowired
  private UserRepository userRepository;
  
  @GetMapping("/")
  @ResponseBody
  public String getPrincipal(Principal principal, @AuthenticationPrincipal User spring_security_user) {
    if (principal == null)
      return "no principal";
    return "Principal got from HTTP HttpServletRequest:<br>" + principal.getName() +
               "<br><br>SecurityContextHolder.getContext().getAuthentication() object:<br>" + SecurityContextHolder.getContext().getAuthentication().toString()
               + "<br><br>@AuthenticationPrincipal User:<br>" + spring_security_user.toString()
               + "<br><br>UderDetails from userRepository.findTopByUsername(principal.getName())/db:<br>" + userRepository.findTopByUsername(principal.getName())
        ;
  }
  
  // TODO: spostare in controller rest TeamController?
  @PostMapping("/propose")
  public String propose_team(@ModelAttribute("command") TeamViewModel teamViewModel,
                             BindingResult bindingResult, Model model) {
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
