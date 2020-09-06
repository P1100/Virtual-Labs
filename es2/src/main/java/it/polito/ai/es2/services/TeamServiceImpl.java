package it.polito.ai.es2.services;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class TeamServiceImpl extends CommonURL implements TeamService {
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

//  /**
//   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
//   */
@Override
@PreAuthorize("hasRole('PROFESSOR') or @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)")
public List<StudentDTO> getMembers(@NotNull Long teamId) {
  log.info("getMembers(" + teamId + ")");
  if (teamId == null) throw new NullParameterException("teamId");
  Optional<Team> team = teamRepository.findById(teamId);
  if (team.isEmpty())
    throw new TeamNotFoundException(teamId);
  return team.get().getStudents().stream().filter(Objects::nonNull).map(y -> modelMapper.map(y, StudentDTO.class)).collect(Collectors.toList());
}

  //  /**
//   * GET {@link APITeams_RestController#getAllTeams()}
//   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public List<TeamDTO> getAllTeams() {
    log.info("getAllTeams()");
    return teamRepository.findAll().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }

  //  /**
//   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getTeam(Long)}
//   */
//  @Override
  @PreAuthorize("hasRole('PROFESSOR') or @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)")
  public Optional<TeamDTO> getTeam(@NotNull Long teamId) {
    log.info("getTeam(" + teamId + ")");
    if (teamId == null) throw new NullParameterException("null team parameter");
    return teamRepository.findById(teamId).map(x -> modelMapper.map(x, TeamDTO.class));
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getTeamsForStudentCourse(Long, String)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  public List<TeamDTO> getTeamsForStudentInCourse(Long studentId, String courseId) {
    log.info("getTeamsForStudent(" + studentId + ")");
    if (studentId == null || courseId == null) throw new NullParameterException(studentId + " " + courseId);
    Optional<Student> optionalStudent = studentRepository.findById(studentId);
    if (optionalStudent.isEmpty()) {
      throw new StudentNotFoundException(studentId);
    }
    Optional<Course> optionalCourse = courseRepository.findById(courseId);
    if (optionalCourse.isEmpty()) {
      throw new CourseNotFoundException(courseId);
    }
    List<Team> teams = optionalStudent.get().getTeams().stream().filter(t -> t.getCourse().getId().equals(courseId)).collect(Collectors.toList());
    for (Team team : teams) {
      if (team.isActive()) {
        if (teams.size() > 1)
          log.warning("Team proposals should be deleted once one is made active (or invalid multiple active teams)");
        continue;
      }
      /* Putting in transient data */
      for (Student student : team.getStudents()) {
        List<Token> tokenList = student.getTokens().stream().filter(t -> t.getTeam().equals(team)).collect(Collectors.toList());
        if (tokenList.size() == 0) {
          log.warning("no tokens associated with student in proposal");
          continue;
        }
        if (tokenList.size() > 1)
          throw new InvalidDataException("Invalid data: Multiple tokens for same Team detected");
        Token tk = tokenList.get(0);
        student.setProposalAccepted(tk.isConfirmed());
        student.setUrlTokenConfirm(tk.getUrlConfirm());
        student.setUrlTokenReject(tk.getUrlReject());
      }
    }
    List<TeamDTO> teamDTOS = teams.stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
    if (teamDTOS.stream().filter(TeamDTO::isActive).count() > 1)
      throw new StudentsInMultipleActiveTeamsException(studentId);
    return teamDTOS;
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#proposeTeam(String, String, List, Long)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT')")
  @Transactional
  public TeamDTO proposeTeam(@NotBlank String courseId, @NotBlank String team_name, @NotNull List<Long> memberIds, @NotNull Long hoursTimeout) {
    log.info("proposeTeam(" + courseId + ", " + team_name + ", " + memberIds + ", " + hoursTimeout + ")");
    if (courseId == null || team_name == null || memberIds == null)
      throw new NullParameterException("proposeTeam (" + courseId + ", " + team_name + ", " + memberIds + ", " + hoursTimeout + ")");
    if (teamRepository.findFirstByNameAndCourse_id(team_name, courseId) != null)
      throw new TeamAlreayCreatedException(team_name, courseId);
    Optional<Course> oc = courseRepository.findById(courseId);
    if (oc.isEmpty()) throw new CourseNotFoundException(courseId);
    Course course = oc.get();
    if (!course.isEnabled()) throw new CourseNotEnabledException(courseId);
    StringBuilder notFoundStudents = new StringBuilder();
    StringBuilder notEnrolledStudents = new StringBuilder();
    StringBuilder alreadyWithTeam = new StringBuilder();
    List<Student> foundStudents = memberIds.stream().map(x -> {
      Optional<Student> op = studentRepository.findById(x);
      if (op.isEmpty())
        notFoundStudents.append(x).append(" ");
      else if (!course.getStudents().contains(op.get()))
        notEnrolledStudents.append(x).append(" ");
      else if (course.getTeams().size() != 0 && course.getTeams().stream().filter(Team::isActive).map(Team::getStudents).flatMap(List::stream)
          .map(Student::getId).distinct().anyMatch(id -> id.equals(op.get().getId())))
        alreadyWithTeam.append(x + " ");
      return op;
    }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    if (notFoundStudents.length() != 0)
      throw new StudentNotFoundException(notFoundStudents.toString());
    if (notEnrolledStudents.length() != 0)
      throw new StudentNotEnrolledException(notEnrolledStudents.toString());
    if (alreadyWithTeam.length() > 1)
      throw new StudentsInMultipleActiveTeamsException(alreadyWithTeam.toString());
    if (alreadyWithTeam.length() == 1)
      throw new StudentAlreadyInTeamException(alreadyWithTeam.toString());

    if (foundStudents.size() < course.getMinSizeTeam())
      throw new CourseCardinalityConstrainsException(courseId, foundStudents.size() + " < " + course.getMinSizeTeam());
    if (foundStudents.size() > course.getMaxSizeTeam())
      throw new CourseCardinalityConstrainsException(courseId, foundStudents.size() + " > " + course.getMaxSizeTeam());
    if (!foundStudents.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException(Arrays.toString(memberIds.toArray()));

    Team team = new Team();
    team.setName(team_name);
    team.setActive(false);
    for (Student student : new ArrayList<>(foundStudents)) {
      team.addStudent(student);
    }
    team.addSetCourse(course);
    Team savedTeam = teamRepository.save(team);
    TeamDTO return_teamDTO = modelMapper.map(savedTeam, TeamDTO.class);
    notifyTeam(hoursTimeout, savedTeam, foundStudents);
    return return_teamDTO;
  }

  private void notifyTeam(@NotNull Long hoursTimeout, @Valid Team savedTeam2, @Valid List<Student> members) {
    Team savedTeam = teamRepository.findById(savedTeam2.getId()).get();
    log.info("notifyTeam(" + modelMapper.map(savedTeam2, TeamDTO.class) + ", " + members.stream().map(students -> modelMapper.map(students, StudentDTO.class)).collect(Collectors.toList()) + ")");
    for (Student student : members) {
      Long memberId = student.getId();
      Token token = new Token();
      token.setId((UUID.randomUUID().toString().toLowerCase()));
      token.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusHours(hoursTimeout)));
      StringBuffer sb = new StringBuffer();
      sb.append("Hello ").append(memberId);
      String urlConfirm = baseUrl + "/notification/team/confirm/" + token.getId();
      sb.append("\n\nLink to accept token:\n" + urlConfirm);
      String urlReject = baseUrl + "/notification/team/reject/" + token.getId();
      sb.append("\n\nLink to remove token:\n" + urlReject);
      System.out.println(sb);
      token.setUrlConfirm(urlConfirm);
      token.setUrlReject(urlReject);
      token.addSetStudent(student);
      token.addSetTeam(savedTeam);
      tokenRepository.save(token);
      String mymatricola = environment.getProperty("mymatricola");
      // TODO: uncommentare in fase di prod (attenzione!)
      System.out.println("[s" + mymatricola + "@studenti.polito.it] s" + memberId + "@studenti.polito.it - Conferma iscrizione al team " + savedTeam.getName());
//        sendMessage("s" + mymatricola + "@studenti.polito.it", "[Student:" + memberId + "] Conferma iscrizione al team " + savedTeam.getName(), sb.toString());
    }
  }

  /**
   * {@link it.polito.ai.es2.controllers.Notification_Controller#confirmUser(String)}
   */
  @Override
  public boolean confirmTeam(@NotBlank String idtoken) {
    Optional<Token> tokenOptional = tokenRepository.findById(idtoken);
    if (tokenOptional.isEmpty())
      return false;
    Token token = tokenOptional.get();
    Team team = token.getTeam();
    if (team == null) {
      log.warning("No team associated with this token: " + token);
      return false;
    }
    if (team.isActive() && team.isDisabled())
      throw new InvalidDataException("Critical data error: Team is both disabled and active");
    if (team.isDisabled())
      return false;
    if (team.isActive()) {
      log.severe("Critical data error: team associated with token is already active");
      return false;
    }
    if (token.isConfirmed() && token.isRejected())
      throw new InvalidDataException("Critical invalid data error: token both confirmed and rejected");
    if (token.isConfirmed() || token.isRejected())
      return false;
    List<Token> tokenList = team.getTokens();
    if (tokenList.size() == 0)
      throw new InvalidDataException("Critical invalid data error: Team has no associated tokens");
    token.setConfirmed(true);
    if (team.getTokens().stream().allMatch(t -> (t.isConfirmed() && !t.isRejected()))) {
      if (team.getStudents().stream().flatMap(x -> x.getTeams().stream().filter(y -> y.getCourse().getId().equals(team.getCourse().getId()))
          .filter(Team::isActive)).count() > 0)
        throw new StudentsInMultipleActiveTeamsException();
      /* Disable all others team proposals, for each students, in the same course */
      team.getStudents().stream().flatMap(x -> x.getTeams().stream().filter(y -> y.getCourse().getId().equals(team.getCourse().getId()))
          .filter(z -> !z.isActive())).forEach(t -> t.setDisabled(true));
      for (Token tok : tokenList) {
        tok.getStudent().getTokens().remove(tok);
      }
      tokenRepository.deleteAll(tokenList);
      team.setActive(true); // no need to save, will be flushed automatically at the end of transaction (since not a new entity)
    }
    return true;
  }

  /**
   * {@link it.polito.ai.es2.controllers.Notification_Controller#rejectTokenTeam(String)}
   */
  @Override
  public boolean rejectTeam(@NotBlank String idtoken) {
    Optional<Token> tokenOptional = tokenRepository.findById(idtoken);
    if (tokenOptional.isEmpty())
      return false;
    Token token = tokenOptional.get();
    Team team = token.getTeam();
    if (team == null) {
      log.warning("No team associated with this token: " + token);
      return false;
    }
    if (team.isActive() && team.isDisabled())
      throw new InvalidDataException("Critical data error: Team is both disabled and active");
    if (team.isDisabled())
      return false;
    if (team.isActive()) {
      log.severe("Critical data error: team associated with token is already active");
      return false;
    }
    if (token.isConfirmed() && token.isRejected())
      throw new InvalidDataException("Critical invalid data error: token both confirmed and rejected");
    if (token.isConfirmed() || token.isRejected())
      return false;
    token.setRejected(true);
    team.setDisabled(true);
    return true;
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#evictTeam(Long)}
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') and  @mySecurityChecker.isTeamOwner(#teamId,authentication.principal.username)")
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
}