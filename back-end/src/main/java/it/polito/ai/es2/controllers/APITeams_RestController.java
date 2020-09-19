package it.polito.ai.es2.controllers;

import it.polito.ai.es2.controllers.hateoas.ModelHelper;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

  @GetMapping("/{student_id}/teams/{courseId}")
  public CollectionModel<TeamDTO> getTeamsUser(@PathVariable Long student_id, @PathVariable String courseId) {
    List<TeamDTO> teams = teamService.getTeamsUser(student_id, courseId);
    for (TeamDTO team : teams) {
      modelHelper.enrich(team);
    }
    CollectionModel<TeamDTO> teamsHAL = CollectionModel.of(teams,
        linkTo(methodOn(APITeams_RestController.class).getTeamsUser(student_id, courseId)).withSelfRel());
    return teamsHAL;
  }

  // http://localhost:8080/api/teams/propose/C0/Team0/100,101,S33
  @PostMapping("/propose/{courseId}/{teamName}/{hoursTimeout}/{memberIds}")
  public TeamDTO proposeTeam(@PathVariable String courseId, @PathVariable String teamName, @PathVariable List<Long> memberIds, @PathVariable Long hoursTimeout) {
    return teamService.proposeTeam(courseId, teamName, memberIds, hoursTimeout);
  }

  @PutMapping()
  public void updateTeam(@Valid @RequestBody TeamDTO teamDTO) {
    teamService.updateTeamConstrains(teamDTO);
  }

  @PostMapping("/evict/{teamId}")
  public boolean evictTeam(@PathVariable Long teamId) {
    return teamService.evictTeam(teamId);
  }

  @DeleteMapping("/cleanup/{courseId}")
  public void cleanupTeams(@NotBlank @PathVariable String courseId) {
    teamService.cleanupTeamsExpiredDisabled(courseId);
  }
}
