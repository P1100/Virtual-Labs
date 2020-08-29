package it.polito.ai.es2.services;

import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * TODO: add formatted email body
 */
@Service
public class NotificationServiceImpl implements NotificationService {
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
/*
  @Override
  public void sendMessage(String emailAddress, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setSubject(subject);
    message.setText(body);
    emailSender.send(message);
  }

  *//**
   * trovare team, e corso?, e rimuovere token
   * if team_corrente ha ancora tokens, ritorna false
   * altrimenti imposta team a status active, e ritorna true
   *//*
  @Override
  @Transactional
  public boolean confirm(String idtoken) {
    Optional<Team> optionalTeam = cleanupAndVerifyTokenExists(idtoken);
    if (optionalTeam.isEmpty())
      return false;
    Team team = optionalTeam.get();
    Long teamId = team.getId();
    Course course = team.getCourse();
    tokenRepository.deleteById(idtoken);
    List<Token> tokenList = tokenRepository.findAllByTeamId(teamId);
    if (tokenList.size() == 0) {
      teamService.setTeamStatus(teamId, true);
      return true;
    }
//    TODO: (??) commented, review later
//    for (Token token : tokenList) {
//      tokenRepository.delete(token);
//    }
    return false;
  }

  *//**
   * Trova team, rimuovi tutti i token relativi a team corrente (se ce ne sono) e invoca evict team + return true. Altrimenti false
   *//*
  @Override
  @Transactional
  public boolean reject(String idtoken) {
    Optional<Team> optionalTeam = cleanupAndVerifyTokenExists(idtoken);
    if (optionalTeam.isEmpty())
      return false;
    Long teamId = optionalTeam.get().getId();
    tokenRepository.deleteAll(tokenRepository.findAllByTeamId(teamId));
    return teamService.evictTeam(teamId);
  }

  *//**
   * Non c'è bisogno di controlli, poichè viene chiamato direttamente da propose team (che fa lui tutti i controlli)
   *//*
  @Override
  @Transactional
  public void notifyTeam(TeamDTO teamDTO, List<Long> memberIds) {
    for (Long memberId : memberIds) {
      Token token = new Token();
      token.setId((UUID.randomUUID().toString()));
      token.setTeamId(teamDTO.getId());
      token.setStudent(studentRepository.findById(memberId).orElse(null));
      token.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
      String url;
      try {
        url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
        tokenRepository.save(token);
      } catch (UnknownHostException e) {
        e.printStackTrace();
        return;
      }
      StringBuffer sb = new StringBuffer();
      sb.append("Hello ").append(memberId);
      sb.append("\n\nLink to accept token:\n" + url + "/notification/confirm/" + token.getId());
      sb.append("\n\nLink to remove token:\n" + url + "/notification/reject/" + token.getId());
      System.out.println(sb);
      String mymatricola = environment.getProperty("mymatricola");
      // TODO: uncommentare in fase di prod (attenzione!)
      System.out.println("[Forced self] s" + mymatricola + "@studenti.polito.it] s" + memberId + "@studenti.polito.it - Conferma iscrizione al team " + teamDTO.getId());
//        sendMessage("s" + mymatricola + "@studenti.polito.it", "[Student:" + memberId + "] Conferma iscrizione al team " + teamDTO.getId(), sb.toString());
    }
  }

  private Optional<Team> cleanupAndVerifyTokenExists(String idtoken) {
    cleanUpOldTokens();
    return tokenRepository.findById(idtoken).map(Token::getTeamId).map(teamId -> teamRepository.getOne(teamId));
  }

  private boolean cleanUpOldTokens() {
    List<Token> tokenExpiredList = tokenRepository.findAllByExpiryDateBeforeOrderByExpiryDate(Timestamp.valueOf(LocalDateTime.now()));
    if (tokenExpiredList.size() > 0) {
      tokenRepository.deleteAll(tokenExpiredList);
      return true;
    } else
      return false;
  }
  */
}
