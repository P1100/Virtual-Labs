package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;

import java.util.List;
import java.util.Optional;

public interface TeamService {
  List<TeamDTO> getAllTeams();
  Optional<TeamDTO> getTeam(Long teamId);
  List<StudentDTO> getMembers(Long TeamId);
  TeamDTO proposeTeam(String courseId, String name, List<Long> memberIds);
  boolean evictTeam(Long teamId);
  
  boolean setTeamStatus(Long teamId, boolean status);
}
