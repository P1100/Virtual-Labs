package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TeamService {

  //  /**
  //   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
  //   */
  @PreAuthorize("hasRole('PROFESSOR') or @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)") List<StudentDTO> getMembers(@NotNull Long teamId);

  //  /**
  //   * GET {@link APITeams_RestController#getAllTeams()}
  //   */
  @PreAuthorize("hasRole('ADMIN')") List<TeamDTO> getAllTeams();

  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  List<TeamDTO> getTeamsForStudentInCourse(Long studentId, String courseId);

  @PreAuthorize("hasRole('STUDENT')")
  TeamDTO proposeTeam(@NotBlank String courseId, @NotBlank String team_name, @NotNull List<Long> memberIds, @NotNull Long hoursTimeout);

  boolean confirmTeam(@NotBlank String token);

  boolean rejectTeam(@NotBlank String idtoken);

  @PreAuthorize("hasRole('ADMIN') and  @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)")
  boolean evictTeam(@NotNull Long teamId);
}
