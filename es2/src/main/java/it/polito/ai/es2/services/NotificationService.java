package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.TeamDTO;

import java.util.List;

/**
 * These could be the problem in your task if you are using Gmail Server.
 * <p>
 * 1-Two Step Verification should be turned off.
 * 2-Allow Less Secure App(should be turned on).
 * 3-Check Your UserName and Password.
 */
public interface NotificationService {
  
  void sendMessage(String address, String subject, String body);
  
  boolean confirm(String token); // per confermare la partecipazione al gruppo
  
  boolean reject(String token); //per esprimere il proprio diniego a partecipare
  
  void notifyTeam(TeamDTO dto, List<String> memberIds);
  
  boolean cleanUpOldTokens();
}
