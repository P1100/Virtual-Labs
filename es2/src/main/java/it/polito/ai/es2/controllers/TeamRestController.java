package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: finire se c'è tempo
@RestController
@RequestMapping("/API/teams")
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
  
  @GetMapping("/{teamId}")
  public TeamDTO getTeam(@PathVariable Long teamId) {
    Optional<TeamDTO> teamDTO = teamService.getTeam(teamId);
    if (!teamDTO.isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, teamId.toString());
    return ModelHelper.enrich(teamDTO.get());
  }
  
  @GetMapping("/{teamId}/members")
  public List<StudentDTO> getMembers(@PathVariable Long teamId) {
    List<StudentDTO> members = teamService.getMembers(teamId);
    for (StudentDTO member : members) {
      ModelHelper.enrich(member);
    }
    return members;
  }
}
