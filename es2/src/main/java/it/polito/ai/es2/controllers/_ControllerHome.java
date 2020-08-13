package it.polito.ai.es2.controllers;

import it.polito.ai.es2.domains.TeamViewModel;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
public class _ControllerHome {
  @Autowired
  TeamService teamService;
  
  @RequestMapping(value = "/API", produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String getAPI() {
    String json = "{\"corsi\":\"http://localhost:8080/API/courses\","
                      + "\"studenti\":\"http://localhost:8080/API/students\","
                      + "\"teams\":\"http://localhost:8080/API/teams\"}";
    return json;
  }
  
  @PostMapping("/propose")
  public String propose_team(@ModelAttribute("command") TeamViewModel teamViewModel,
                             BindingResult bindingResult, Model model) {
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
    return "csv_home";
  }
}
