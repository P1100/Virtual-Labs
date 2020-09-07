package it.polito.ai.es2.services;

import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

// TODO: add formatted email body
@Service
@Validated
@PreAuthorize("permitAll()")
public class NotificationServiceImpl implements NotificationService {
  @Autowired
  public JavaMailSender emailSender;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  public TokenRepository tokenRepository;

  @Override
  public void sendMessage(@NotBlank String emailAddress, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setSubject(subject);
    message.setText(body);
    emailSender.send(message);
  }
}
