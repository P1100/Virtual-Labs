package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.domains.TeamViewModel;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/API/teams")
public class APITeams_RestController {
  @Autowired
  TeamService teamService;
  @Autowired
  private ModelHelper modelHelper;
  
  @GetMapping({"", "/"})
  public CollectionModel<TeamDTO> getAllTeams() {
    List<TeamDTO> allTeams = teamService.getAllTeams();
    for (TeamDTO teamDTO : allTeams) {
      modelHelper.enrich(teamDTO);
    }
    Link link = linkTo(methodOn(APITeams_RestController.class)
                           .getAllTeams()).withSelfRel();
    CollectionModel<TeamDTO> result = new CollectionModel<>(allTeams, link);
    return result;
  }
  
  @GetMapping("/{teamId}")
  public TeamDTO getTeam(@PathVariable Long teamId) {
    Optional<TeamDTO> teamDTO = teamService.getTeam(teamId);
    if (!teamDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, teamId.toString());
    return modelHelper.enrich(teamDTO.get());
  }
  
  @GetMapping("/{teamId}/members")
  public List<StudentDTO> getMembers(@PathVariable Long teamId) {
    List<StudentDTO> members = teamService.getMembers(teamId);
    for (StudentDTO member : members) {
      modelHelper.enrich(member);
    }
    return members;
  }
  
  // http://localhost:8080/API/teams/propose/C0/Team0/100,101,S33
  @PostMapping("/propose/{courseName}/{team_name}/{memberIds}")
  public TeamDTO proposeTeam(@PathVariable String courseName, @PathVariable String team_name, @PathVariable List<Long> memberIds) {
    return teamService.proposeTeam(courseName, team_name, memberIds);
  }
  
  @PostMapping("/evict/{teamId}")
  public boolean evictTeam(@PathVariable Long teamId) {
    return teamService.evictTeam(teamId);
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
