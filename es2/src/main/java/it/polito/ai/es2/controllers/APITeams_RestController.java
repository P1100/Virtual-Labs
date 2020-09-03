package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/teams")
public class APITeams_RestController {
  @Autowired
  TeamService teamService;
  @Autowired
  private ModelHelper modelHelper;

  @GetMapping("/{teamId}/members")
  public List<StudentDTO> getMembers(@PathVariable Long teamId) {
    List<StudentDTO> members = teamService.getMembers(teamId);
    for (StudentDTO member : members) {
      modelHelper.enrich(member);
    }
    return members;
  }

  @GetMapping({"", "/"})
  public CollectionModel<TeamDTO> getAllTeams() {
    List<TeamDTO> allTeams = teamService.getAllTeams();
    for (TeamDTO teamDTO : allTeams) {
      modelHelper.enrich(teamDTO);
    }
    Link link = linkTo(methodOn(APITeams_RestController.class)
                           .getAllTeams()).withSelfRel();
    CollectionModel<TeamDTO> result = CollectionModel.of(allTeams, link);
    return result;
  }

  @GetMapping("/{teamId}")
  public TeamDTO getTeam(@PathVariable Long teamId) {
    Optional<TeamDTO> teamDTO = teamService.getTeam(teamId);
    if (teamDTO.isEmpty())
      throw new ResponseStatusException(HttpStatus.CONFLICT, teamId.toString());
    return modelHelper.enrich(teamDTO.get());
  }

  // http://localhost:8080/api/teams/propose/C0/Team0/100,101,S33
  @PostMapping("/propose/{courseName}/{team_name}/{memberIds}")
  public TeamDTO proposeTeam(@PathVariable String courseName, @PathVariable String team_name, @PathVariable List<Long> memberIds) {
    return teamService.proposeTeam(courseName, team_name, memberIds);
  }

  @PostMapping("/evict/{teamId}")
  public boolean evictTeam(@PathVariable Long teamId) {
    return teamService.evictTeam(teamId);
  }
}
