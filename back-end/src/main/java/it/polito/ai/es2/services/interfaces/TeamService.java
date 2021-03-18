package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TeamService {
  List<StudentDTO> getMembers(@NotNull Long teamId);

  void updateTeamConstrains(@Valid TeamDTO teamDTO);

  List<TeamDTO> getAllActiveTeamsForCourse(@NotNull String courseId);

  List<StudentDTO> getEnrolledWithoutTeam(String courseName);

  List<TeamDTO> getTeamsUser(Long studentId, String courseId);

  TeamDTO proposeTeam(@NotBlank String courseId, @NotBlank String team_name, @NotNull List<Long> memberIds, @NotNull Long hoursTimeout);

  boolean confirmTeam(@NotBlank String token);

  boolean rejectTeam(@NotBlank String idtoken);

  boolean evictTeam(@NotNull Long teamId);

  void cleanupTeamsExpiredDisabled(@NotBlank String courseId);
}
