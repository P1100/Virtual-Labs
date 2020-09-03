package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface TeamService {
  List<StudentDTO> getMembers(@NotNull Long TeamId);

  List<TeamDTO> getAllTeams();

  Optional<TeamDTO> getTeam(@NotNull Long teamId);

  TeamDTO proposeTeam(@NotBlank String courseId, @NotBlank String name, @NotNull List<Long> memberIds);

  boolean evictTeam(@NotNull Long teamId);

  boolean confirmTeam(@NotBlank String token);

  boolean rejectTeam(@NotBlank String idtoken);

  void notifyTeam(@Valid TeamDTO teamDTO, @NotNull List<Long> memberIds);
}
