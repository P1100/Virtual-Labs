package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface TeamService {
  @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)")
  List<StudentDTO> getMembers(Long TeamId);

/*
  @PreAuthorize("hasRole('ADMIN')")
  List<TeamDTO> getAllTeams();

  @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)")
  Optional<TeamDTO> getTeam(Long teamId);



  // TODO: credo di aver fatto in modo che non ci possono essere piú gruppi con lo stesso nome per lo stesso corso, check in fase di creazione
  @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
  TeamDTO proposeTeam(String courseId, String name, List<Long> memberIds);

  @PreAuthorize("hasRole('ADMIN') or (hasRole('PROFESSOR') and @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username))")
  boolean evictTeam(Long teamId);

  boolean setTeamStatus(Long teamId, boolean status);
 */
}
