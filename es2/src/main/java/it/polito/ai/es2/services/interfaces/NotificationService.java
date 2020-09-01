package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.dtos.UserDTO;

import java.util.List;

/**
 * These could be the problem in your task if you are using Gmail Server.
 * <p>
 * 1-Two Step Verification should be turned off.
 * 2-Allow Less Secure App(should be turned on).
 * 3-Check Your UserName and Password.
 */
public interface NotificationService {
//  void sendMessage(String address, String subject, String body);

  boolean confirmTeam(String token); // per confermare la partecipazione al gruppo

  boolean rejectTeam(String token); //per esprimere il proprio diniego a partecipare

  void notifyTeam(TeamDTO dto, List<Long> memberIds);

  boolean confirmUser(String token);

  void notifyUser(UserDTO userDTO);
}
