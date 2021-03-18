package it.polito.ai.es2.services.interfaces;

import javax.validation.constraints.NotBlank;

/**
 * These could be the problem in your task if you are using Gmail Server.
 * <p>
 * 1-Two Step Verification should be turned off.
 * 2-Allow Less Secure App(should be turned on).
 * 3-Check Your UserName and Password.
 */
public interface NotificationService {
  void sendMessage(@NotBlank String emailAddress, String subject, String body);
}
