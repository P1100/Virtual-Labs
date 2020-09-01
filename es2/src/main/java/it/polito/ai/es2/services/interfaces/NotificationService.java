package it.polito.ai.es2.services.interfaces;

/**
 * These could be the problem in your task if you are using Gmail Server.
 * <p>
 * 1-Two Step Verification should be turned off.
 * 2-Allow Less Secure App(should be turned on).
 * 3-Check Your UserName and Password.
 */
public interface NotificationService {
  boolean cleanUpOldTokens();

  void sendMessage(String address, String subject, String body);



}
