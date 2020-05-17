package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Token;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
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
@Transactional
public class NotificationServiceImpl implements NotificationService {
  private final boolean forceOutputEmail_testing = true;
  @Autowired
  public JavaMailSender emailSender;
  @Autowired
  public TokenRepository tokenRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  TeamRepository teamRepository;
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
  
  private Token verifyTokenExists(String idtoken) {
    cleanUpOldTokens();
    Optional<Token> token = tokenRepository.findById(idtoken);
    if (token.isEmpty())
      return null;
    return token.get();
  }
  
  @Override
  public boolean confirm(String idtoken) {
    Token token = verifyTokenExists(idtoken);
    if (token == null)
      return false;
    // trovare team, e corso?, e rimuovere token
    // if team_corrente ha ancora tokens, ritorna false
    // altrimenti imposta team a status active, rimuovi tutti i team duplicati (per sicurezza), e ritorna true
//    (token.getTeamId())
//    getTokensForTeam()
    return false;
  }
  
  @Override
  public boolean reject(String idtoken) {
    Token token = verifyTokenExists(idtoken);
    if (token == null)
      return false;
    // Trova team, rimuovi tutti i token relativi a team corrente (se ce ne sono) e invocare evict team e return true.
    //altrimenti false
    return false;
  }
  
  // TODO: inserire collegamento tra tokens e team/studenti. Controllare che team non sia gia stato creato o in corso.
  //  Controllo che student esista.
  // Nota: teamDTO e memberIds sono gli stessi passati a proposeTeam, quindi dovrebbero essere giusti...
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
  
  @Override
  public boolean cleanUpOldTokens() {
    List<Token> tokenExpiredList = tokenRepository.findAllByExpiryDateBeforeOrderByExpiryDate(Timestamp.valueOf(LocalDateTime.now()));
    if (tokenExpiredList.size() > 0) {
      tokenRepository.deleteAll(tokenExpiredList);
      return true;
    } else
      return false;
  }
  
  private List<Token> getTokensForTeam(Long teamId) {
    List<Token> tokenList = tokenRepository.findAllByTeamId(teamId);
    return tokenList;
  }
}