package it.polito.ai.es2.controllers;

import it.polito.ai.es2.domains.TeamViewModel;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.securityconfig.UserDetailsServiceImpl;
import it.polito.ai.es2.securityconfig.UserRepository;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
  //UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  public String getPrincipal(Principal principal) {
    if (principal == null)
      return "no principal";
    return principal.getName() +
               "<br>Authentication object:<br>" + SecurityContextHolder.getContext().getAuthentication().toString()
               + "<br><br>UderDetails from principal_name/db:<br>" + userRepository.findTopByUsername(principal.getName())
        ;
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
