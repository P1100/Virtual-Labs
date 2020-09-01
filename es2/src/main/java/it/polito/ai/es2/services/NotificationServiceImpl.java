package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.dtos.UserDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.entities.Token;
import it.polito.ai.es2.entities.User;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: add formatted email body
@Service
@Validated
public class NotificationServiceImpl implements NotificationService {
  @Autowired
  public JavaMailSender emailSender;
  @Autowired
  public TokenRepository tokenRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  TeamService teamService;
  @Autowired
  private ModelMapper modelMapper;
  @Value("${server.port}")
  String port;
  @Autowired
  Environment environment;

  @Override
  @Transactional
  public boolean confirmTeam(@NotBlank String token) {
    cleanUpOldTokens();
    Optional<Team> optionalTeam = tokenRepository.findById(token).map(Token::getTeamId).map(teamId1 -> teamRepository.getOne(teamId1));
    if (optionalTeam.isEmpty())
      return false;
    Team team = optionalTeam.get();
    Long teamId = team.getId();
    Course course = team.getCourse();
    tokenRepository.deleteById(token);
    List<Token> tokenList = tokenRepository.findAllByTeamId(teamId);
    if (tokenList.size() == 0) {
      if (team == null || team.isActive() == true) // ??
        return false;
      team.setActive(true); // no need to save, will be flushed automatically at the end of transaction (since not a new entity)
      return true;
    }
//    TODO: (??) commented, review later
//    for (Token token : tokenList) {
//      tokenRepository.delete(token);
//    }
    return false;
  }

  @Override public boolean confirmUser(@NotBlank String token) {
    cleanUpOldTokens();
//    Optional<UserDTO> optionalUserDTO = tokenRepository.findById(token).map(Token::getUser).map(x -> modelMapper.map(x, UserDTO.class));
    Optional<User> userOptional = tokenRepository.findById(token).map(Token::getUser);
    if (userOptional.isEmpty()) {

    }
    return false;
  }

  @Override
  public void notifyUser(UserDTO userDTO) {
//    Token token = new Token();
//    token.setId((UUID.randomUUID().toString().toLowerCase()));
//    token.setUser(savedUser);
//    token.setTeamId(null);
//    token.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));
//    tokenRepository.save(token);
//
//    StringBuffer sb = new StringBuffer();
//    sb.append("Hello ").append(userDTO.getFirstName() + ' ' + userDTO.getLastName() + userDTO.getUsername());
//    if (role.equals("student")) {
//      sb.append("\n\nLink to accept token:\n" + baseUrl + "/notification/user/confirm/" + token.getId());
//      sb.append("\n\nLink to remove token:\n" + url + "/notification/user/reject/" + token.getId());
//    } else { // professor
//      sb.append("\n\nLink to accept token:\n" + url + "/notification/confirm/" + token.getId());
//      sb.append("\n\nLink to remove token:\n" + url + "/notification/reject/" + token.getId());
//    }
//    System.out.println(sb);
//    String mymatricola = environment.getProperty("mymatricola");
//    // TODO: uncommentare in fase di prod (attenzione!)
//    System.out.println("[Forced self] s" + mymatricola + "@studenti.polito.it] s" + memberId + "@studenti.polito.it - Conferma iscrizione al team " + teamDTO.getId());
////        sendMessage("s" + mymatricola + "@studenti.polito.it", "[Student:" + memberId + "] Conferma iscrizione al team " + teamDTO.getId(), sb.toString());
  }

  /**
   * Trova team, rimuovi tutti i token relativi a team corrente (se ce ne sono) e invoca evict team + return true. Altrimenti false
   */
  @Override
  @Transactional
  public boolean rejectTeam(@NotBlank String idtoken) {
    cleanUpOldTokens();
    Optional<Team> optionalTeam = tokenRepository.findById(idtoken).map(Token::getTeamId).map(teamId1 -> teamRepository.getOne(teamId1));
    if (optionalTeam.isEmpty())
      return false;
    Long teamId = optionalTeam.get().getId();
    tokenRepository.deleteAll(tokenRepository.findAllByTeamId(teamId));
    return teamService.evictTeam(teamId);
  }

  /**
   * Non c'è bisogno di controlli, poichè viene chiamato direttamente da propose team (che fa lui tutti i controlli)
   */
  @Override
  @Transactional
  public void notifyTeam(@Valid TeamDTO teamDTO, @NotNull List<Long> memberIds) {
    for (Long memberId : memberIds) {
      Token token = new Token();
      token.setId((UUID.randomUUID().toString().toLowerCase()));
      token.setTeamId(teamDTO.getId());
//      token.setStudent(studentRepository.findById(memberId).orElse(null));
      token.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
      String url;
      try {
        // TODO: does it work in production?
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

  private boolean cleanUpOldTokens() {
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
