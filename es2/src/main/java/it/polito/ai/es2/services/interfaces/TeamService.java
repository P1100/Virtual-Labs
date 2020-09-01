package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface TeamService {
  List<StudentDTO> getMembers(Long TeamId);

  // **************************** -- not reviewd --------------------
  List<TeamDTO> getAllTeams();

  Optional<TeamDTO> getTeam(Long teamId);

  TeamDTO proposeTeam(String courseId, String name, List<Long> memberIds);

  boolean evictTeam(Long teamId);

  boolean confirmTeam(@NotBlank String token);

  boolean rejectTeam(@NotBlank String idtoken);

  void notifyTeam(@Valid TeamDTO teamDTO, @NotNull List<Long> memberIds);
}
