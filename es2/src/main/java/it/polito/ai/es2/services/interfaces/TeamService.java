package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface TeamService {
  List<StudentDTO> getMembers(@NotNull Long TeamId);

  List<TeamDTO> getAllTeams();

  Optional<TeamDTO> getTeam(@NotNull Long teamId);

  @PreAuthorize("hasRole('STUDENT')") TeamDTO proposeTeam(@NotBlank String courseName, @NotBlank String team_name, @NotNull List<Long> memberIds, @NotNull Long hoursTimeout);

  boolean evictTeam(@NotNull Long teamId);

  boolean confirmTeam(@NotBlank String token);

  boolean rejectTeam(@NotBlank String idtoken);
}
