package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.entities.Token;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO: add formatted email body
 */
@Service
//@Transactional
public class NotificationServiceImpl implements NotificationService {
  private final boolean forceOutputEmail_testing = true;
  @Autowired
  public JavaMailSender emailSender;
  @Autowired
  public TokenRepository tokenRepository;
  @Autowired
  TeamService teamService;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  StudentRepository studentRepository;
  @Value("${server.port}")
  String port;
  @Autowired
  Environment environment;
  
  @Override
  public void sendMessage(String emailAddress, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setSubject(subject);
    message.setText(body);
    emailSender.send(message);
  }
  
  /**
   * trovare team, e corso?, e rimuovere token
   * if team_corrente ha ancora tokens, ritorna false
   * altrimenti imposta team a status active, e ritorna true
   */
  @Override
  @Transactional
  public boolean confirm(String idtoken) {
    Optional<Team> optionalTeam = cleanupAndVerifyTokenExists(idtoken);
    if (!optionalTeam.isPresent())
      return false;
    Team team = optionalTeam.get();
    Long teamId = team.getId();
    Course course = team.getCourse();
    tokenRepository.deleteById(idtoken);
    List<Token> tokenList = tokenRepository.findAllByTeamId(teamId);
    if (tokenList.size() == 0) {
      teamService.setTeamStatus(teamId, Team.status_active());
      return true;
    }
    for (Token token : tokenList) {
      tokenRepository.delete(token);
    }
    return false;
  }
  
  /**
   * Trova team, rimuovi tutti i token relativi a team corrente (se ce ne sono) e invoca evict team + return true. Altrimenti false
   */
  @Override
  @Transactional
  public boolean reject(String idtoken) {
    Optional<Team> optionalTeam = cleanupAndVerifyTokenExists(idtoken);
    if (!optionalTeam.isPresent())
      return false;
    Long teamId = optionalTeam.get().getId();
    tokenRepository.deleteAll(tokenRepository.findAllByTeamId(teamId));
    return teamService.evictTeam(teamId);
  }
  
  /**
   * Non c'è bisogno di controlli, poichè viene chiamato direttamente da propose team (che fa lui tutti i controlli)
   */
  @Override
  public void notifyTeam(TeamDTO teamDTO, List<String> memberIds) {
    for (String memberId : memberIds) {
      Token token = new Token((UUID.randomUUID().toString()), teamDTO.getId(),
          Timestamp.valueOf(LocalDateTime.now().plusHours(1)), studentRepository.findById(memberId).orElseGet(() -> null));
      String url = null;
      try {
        url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
        tokenRepository.save(token);
      } catch (UnknownHostException e) {
        e.printStackTrace();
        return;
      }
      StringBuffer sb = new StringBuffer();
      sb.append("Hello " + memberId);
      sb.append("\n\nLink to accept token:\n" + url + "/notification/confirm/" + token.getId());
      sb.append("\n\nLink to remove token:\n" + url + "/notification/reject/" + token.getId());
      System.out.println(sb);
      // TODO: uncommentare in fase di prod
      if (forceOutputEmail_testing == false) {
        System.out.println("[regular email] s" + memberId + "@studenti.polito.it - Conferma iscrizione al team " + teamDTO.getId());
//        sendMessage("s" + memberId + "@studenti.polito.it", "Conferma iscrizione al team " + teamDTO.getId(), sb.toString());
      } else {
        String mymatricola = environment.getProperty("mymatricola");
        System.out.println("[Forced self s" + mymatricola + "@studenti.polito.it] s" + memberId + "@studenti.polito.it - Conferma iscrizione al team " + teamDTO.getId());
//        sendMessage("s" + mymatricola + "@studenti.polito.it", "[TESTING FORCE SELF EMAIL - Student:" + memberId + "] Conferma iscrizione al team " + teamDTO.getId(), sb.toString());
      }
    }
  }
  
  private Optional<Team> cleanupAndVerifyTokenExists(String idtoken) {
    cleanUpOldTokens();
    return tokenRepository.findById(idtoken).map(token -> token.getTeamId()).map(teamId -> teamRepository.getOne(teamId));
  }
  
  private boolean cleanUpOldTokens() {
    List<Token> tokenExpiredList = tokenRepository.findAllByExpiryDateBeforeOrderByExpiryDate(Timestamp.valueOf(LocalDateTime.now()));
    if (tokenExpiredList.size() > 0) {
      tokenRepository.deleteAll(tokenExpiredList);
      return true;
    } else
      return false;
  }
}