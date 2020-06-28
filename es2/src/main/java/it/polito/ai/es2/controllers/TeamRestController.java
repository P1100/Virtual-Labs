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
@RequestMapping("/API/groups")
public class TeamRestController {
  @Autowired
  TeamService teamService;
  
  @GetMapping({"", "/"})
  public CollectionModel<TeamDTO> getAllTeams() {
    List<TeamDTO> allTeams = teamService.getAllTeams();
    for (TeamDTO teamDTO : allTeams) {
      ModelHelper.enrich(teamDTO);
    }
    Link link = linkTo(methodOn(TeamRestController.class)
                           .getAllTeams()).withSelfRel();
    CollectionModel<TeamDTO> result = new CollectionModel<>(allTeams, link);
    return result;
  }
  
  @GetMapping("/{groupId}")
  public TeamDTO getTeam(@PathVariable Long groupId) {
    Optional<TeamDTO> teamDTO = teamService.getTeam(groupId);
    if (!teamDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, groupId.toString());
    return ModelHelper.enrich(teamDTO.get());
  }
  
  @GetMapping("/{groupId}/members")
  public List<StudentDTO> getMembers(@PathVariable Long groupId) {
    List<StudentDTO> members = teamService.getMembers(groupId);
    for (StudentDTO member : members) {
      ModelHelper.enrich(member);
    }
    return members;
  }
  
  // http://localhost:8080/API/groups/propose/C0/Team0/100,101,S33
  @PostMapping("/propose/{courseName}/{team_name}/{memberIds}")
  public TeamDTO proposeTeam(@PathVariable String courseName, @PathVariable String team_name, @PathVariable List<String> memberIds) {
    return teamService.proposeTeam(courseName, team_name, memberIds);
  }
  
  @PostMapping("/evict/{groupId}")
  public boolean evictTeam(@PathVariable Long groupId) {
    return teamService.evictTeam(groupId);
  }
}
