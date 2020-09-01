package it.polito.ai.es2.services;

import it.polito.ai.es2.controllers.APITeams_RestController;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.entities.Token;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.TokenRepository;
import it.polito.ai.es2.services.exceptions.*;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Descrizione classe<p>Politica di sovrascrittura adottata: in quasi tutti i metodi add, se un id era già presente nel database non sovrascrivo i dati
 * già esistenti (tranne nel caso di proposeTeam, che poichè ha un id autogenerato, si è deciso di aggiornare il team vecchio usando
 * sempre la proposeTeam).
 */
@Service
@Transactional
@Log
@Validated
public class TeamServiceImpl implements TeamService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  NotificationService notificationService;
  //  @Autowired
//  AssignmentRepository assignmentRepository;
//  @Autowired
//  ImageRepository imageRepository;
//  @Autowired
//  ImplementationRepository implementationRepository;
//  @Autowired
//  VMRepository vmRepository;
  @Autowired
  public TokenRepository tokenRepository;
  @Value("${server.port}")
  String port;
  @Autowired
  Environment environment;

  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
   */
  @Override
  public List<StudentDTO> getMembers(@NotNull Long teamId) {
    log.info("getMembers(" + teamId + ")");
    if (teamId == null) throw new NullParameterException("teamId");
    Optional<Team> team = teamRepository.findById(teamId);
    if (team.isEmpty())
      throw new TeamNotFoundException(teamId);
    return team.get().getStudents().stream().filter(Objects::nonNull).map(y -> modelMapper.map(y, StudentDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link APITeams_RestController#getAllTeams()}
   */
  @Override
  public List<TeamDTO> getAllTeams() {
    log.info("getAllTeams()");
    return teamRepository.findAll().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getTeam(Long)}
   */
  @Override
  public Optional<TeamDTO> getTeam(@NotNull Long teamId) {
    log.info("getTeam(" + teamId + ")");
    if (teamId == null) throw new NullParameterException("null team parameter");
    return teamRepository.findById(teamId).map(x -> modelMapper.map(x, TeamDTO.class));
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#proposeTeam(String, String, List)}
   */
  @Override
  public TeamDTO proposeTeam(@NotBlank String courseName, @NotBlank String team_name, @NotNull List<Long> memberIds) {
    log.info("proposeTeam(" + courseName + ", " + team_name + ", " + memberIds + ")");
    if (courseName == null || team_name == null || memberIds == null)
      throw new NullParameterException("null student or course or list of memberIds parameter");
    Optional<Course> oc = courseRepository.findById(courseName);
    if (oc.isEmpty()) throw new CourseNotFoundException("proposeTeam - course not found");
    if (!oc.get().isEnabled()) throw new CourseNotEnabledException("proposeTeam() - course not enabled");
    List<Optional<Student>> streamopt_listStudentsProposal = memberIds.stream().map(x -> studentRepository.findById(x)).collect(Collectors.toList());
    if (!streamopt_listStudentsProposal.stream().allMatch(Optional::isPresent))
      throw new StudentNotFoundException("proposeTeam() - student not found");

    Course course = oc.get();
    List<Student> listStudentsProposal = streamopt_listStudentsProposal.stream().map(Optional::get).collect(Collectors.toList());
    if (!course.getStudents().containsAll(listStudentsProposal)) // !listStudentsProposal.stream().allMatch(x -> course.getStudents().contains(x))
      throw new StudentNotEnrolledException("proposeTeam() - non tutti gli studenti sono iscritti al corso " + course.getId());
    // Controllo se tra gli studenti dei vari teams del corso, ce n'è qualcuno tra quelli presenti nella proposta
    if (course.getTeams().size() != 0 &&
            course.getTeams()
                .stream()
                .map(Team::getStudents)
                .flatMap(List::stream)
                .distinct()
                .anyMatch(student -> listStudentsProposal.stream().anyMatch(student::equals))
    )
      throw new StudentInMultipleTeamsException("proposeTeam() - studenti fanno parte di altri gruppi nell’ambito dello stesso corso");
    if (listStudentsProposal.size() < course.getMinSizeTeam() || listStudentsProposal.size() > course.getMaxSizeTeam())
      throw new CourseCardinalityConstrainsException(course.getId(), "Proposal rejected");
    if (!listStudentsProposal.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException("proposeTeam() - duplicati nell'elenco dei partecipanti della proposta team");
    if (teamRepository.findFirstByNameAndCourse_id(team_name, courseName) != null)
      throw new TeamAlreayCreatedException("proposeTeam() - team già creato");

    TeamDTO teamDTO = new TeamDTO();
    teamDTO.setName(team_name);
    teamDTO.setActive(false);
    Team new_team = modelMapper.map(teamDTO, Team.class);
    // aggiungo nuovo team, a studenti e al corso
    for (Student student : new ArrayList<>(listStudentsProposal)) {
      new_team.addStudent(student); //student.addTeam(new_team); // add su studenti
    }
    new_team.addSetCourse(course); //course.addTeam(new_team); // add sul singolo corso
    Team savedTeam = teamRepository.save(new_team);
    TeamDTO return_teamDTO = modelMapper.map(savedTeam, TeamDTO.class);
    notifyTeam(return_teamDTO, memberIds);
    return return_teamDTO;
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#evictTeam(Long)}
   */
  @Override
  public boolean evictTeam(@NotNull Long teamId) {
    log.info("evictTeam(" + teamId + ")");
    Optional<Team> optionalTeam = teamRepository.findById(teamId);
    if (optionalTeam.isEmpty())
      return false;
    Team team_to_delete = optionalTeam.get();

    for (Student student : team_to_delete.getStudents()) {
      // usare "student.removeTeam()" rimuoverebbe studenti da team, il che creerebbe problemi in quanto modificherebbe il ciclo foreach enhanced in corso (java.util.ConcurrentModificationException)
      student.getTeams().remove(team_to_delete);
    }
    // --> non serve rimuovere students e course da team, perchè tanto lo cancello
    team_to_delete.getCourse().getTeams().remove(team_to_delete);
    teamRepository.delete(team_to_delete);
    return true;
  }

  @Override
  public boolean confirmTeam(@NotBlank String token) {
    notificationService.cleanUpOldTokens();
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

  /**
   * Trova team, rimuovi tutti i token relativi a team corrente (se ce ne sono) e invoca evict team + return true. Altrimenti false
   */
  @Override
  public boolean rejectTeam(@NotBlank String idtoken) {
    notificationService.cleanUpOldTokens();
    Optional<Team> optionalTeam = tokenRepository.findById(idtoken).map(Token::getTeamId).map(teamId1 -> teamRepository.getOne(teamId1));
    if (optionalTeam.isEmpty())
      return false;
    Long teamId = optionalTeam.get().getId();
    tokenRepository.deleteAll(tokenRepository.findAllByTeamId(teamId));
    return evictTeam(teamId);
  }

  /**
   * Non c'è bisogno di controlli, poichè viene chiamato direttamente da propose team (che fa lui tutti i controlli)
   */
  @Override
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
}