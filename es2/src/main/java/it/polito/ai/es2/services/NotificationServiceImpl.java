package it.polito.ai.es2.services;

import it.polito.ai.es2.entities.Token;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

// TODO: add formatted email body
@Service
@Validated
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
  public boolean cleanUpOldTokens() {
    List<Token> tokenExpiredList = tokenRepository.findAllByExpiryDateBeforeOrderByExpiryDate(Timestamp.valueOf(LocalDateTime.now()));
    if (tokenExpiredList.size() > 0) {
      tokenRepository.deleteAll(tokenExpiredList);
      return true;
    } else
      return false;
  }

  public void sendMessage(@NotBlank String emailAddress, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setSubject(subject);
    message.setText(body);
    emailSender.send(message);
  }
}
