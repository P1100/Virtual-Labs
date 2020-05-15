package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: add formatted email body
 */
@Service
public class NotificationServiceImpl implements NotificationService {
  @Autowired
  public JavaMailSender emailSender;
  
  @Override
  public void sendMessage(String address, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(address);
    message.setSubject(subject);
    message.setText(body);
    emailSender.send(message);
  }
  
  @Override
  public boolean confirm(String token) {
    return false;
  }
  
  @Override
  public boolean reject(String token) {
    return false;
  }
  
  @Override
  public void notifyTeam(TeamDTO dto, List<String> memberIds) {
  
  }
}
