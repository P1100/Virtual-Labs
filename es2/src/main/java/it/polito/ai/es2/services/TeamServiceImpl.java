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

  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
   */
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

  /**
   * GET {@link APITeams_RestController#getAllTeams()}
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public List<TeamDTO> getAllTeams() {
    log.info("getAllTeams()");
    return teamRepository.findAll().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getTeam(Long)}
   */
  @Override
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
    List<TeamDTO> teamDTOS = optionalStudent.get().getTeams().stream().filter(t -> t.getCourse().getId() == courseId)
        .map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
    if (teamDTOS.stream().filter(x->x.isActive()).count() > 1)
      throw new StudentInMultipleActiveTeamsException(studentId);
    return teamDTOS;
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#proposeTeam(String, String, List, Long)}
   */
  @Override
  @PreAuthorize("hasRole('STUDENT')")
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
    List<Student> listFoundStudents = memberIds.stream().map(x -> {
      Optional<Student> op = studentRepository.findById(x);
      if (op.isEmpty())
        notFoundStudents.append(x + " ");
      else if (!course.getStudents().contains(op.get()))
        notEnrolledStudents.append(x + " ");
      else if (course.getTeams().size() != 0 && course.getTeams().stream().filter(Team::isActive).map(Team::getStudents).flatMap(List::stream)
          .map(Student::getId).distinct().anyMatch(id -> id.equals(op.get().getId())))
        alreadyWithTeam.append(x + " ");
      return op;
    }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    if (notFoundStudents.length() != 0)
      throw new StudentNotFoundException(notFoundStudents.toString());
    if (notEnrolledStudents.length() != 0)
      throw new StudentNotEnrolledException(notEnrolledStudents.toString());
    if (alreadyWithTeam.length() != 0)
      throw new StudentInMultipleActiveTeamsException(alreadyWithTeam.toString());

    if (listFoundStudents.size() < course.getMinSizeTeam())
      throw new CourseCardinalityConstrainsException(courseId, listFoundStudents.size() + " < " + course.getMinSizeTeam());
    if (listFoundStudents.size() > course.getMaxSizeTeam())
      throw new CourseCardinalityConstrainsException(courseId, listFoundStudents.size() + " > " + course.getMaxSizeTeam());
    if (!listFoundStudents.stream().allMatch(new HashSet<>()::add))
      throw new StudentDuplicatesInProposalException(Arrays.toString(memberIds.toArray()));

    Team team = new Team();
    team.setName(team_name);
    team.setActive(false);
    team.setCourse(course);
    for (Student student : new ArrayList<>(listFoundStudents)) {
      team.addStudent(student);
    }
    team.addSetCourse(course);
    Team savedTeam = teamRepository.saveAndFlush(team);
    TeamDTO return_teamDTO = modelMapper.map(savedTeam, TeamDTO.class);
    notifyTeam(return_teamDTO, memberIds, hoursTimeout, savedTeam);
    return return_teamDTO;
  }

  private void notifyTeam(@Valid TeamDTO teamDTO, @NotNull List<Long> memberIds, @NotNull Long hoursTimeout, @Valid Team savedTeam) {
    log.info("notifyTeam(" + teamDTO + ", " + memberIds + ")");
    for (Long memberId : memberIds) {
      Token token = new Token();
      token.setId((UUID.randomUUID().toString().toLowerCase()));
      token.addSetTeam(savedTeam);
      token.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusHours(hoursTimeout)));
      tokenRepository.save(token);
      StringBuffer sb = new StringBuffer();
      sb.append("Hello ").append(memberId);
      sb.append("\n\nLink to accept token:\n" + baseUrl + "/notification/team/confirm/" + token.getId());
      sb.append("\n\nLink to remove token:\n" + baseUrl + "/notification/team/reject/" + token.getId());
      System.out.println(sb);
      String mymatricola = environment.getProperty("mymatricola");
      // TODO: uncommentare in fase di prod (attenzione!)
      System.out.println("[s" + mymatricola + "@studenti.polito.it] s" + memberId + "@studenti.polito.it - Conferma iscrizione al team " + teamDTO.getId());
//        sendMessage("s" + mymatricola + "@studenti.polito.it", "[Student:" + memberId + "] Conferma iscrizione al team " + teamDTO.getId(), sb.toString());
    }
  }

  /**
   * {@link it.polito.ai.es2.controllers.Notification_Controller#confirmUser(String)}
   */
  @Override
  public boolean confirmTeam(@NotBlank String token) {
    notificationService.cleanUpOldTokens();
    Optional<Team> optionalTeam = tokenRepository.findById(token).map(Token::getTeam);
    if (optionalTeam.isEmpty())
      return false;
    Team team = optionalTeam.get();
    tokenRepository.deleteById(token);
    List<Token> tokenList = team.getTokens();
    if (tokenList.size() == 0) {
      if (team.isActive()) {
        log.severe("Notification error: team was already active");
        return false;
      }
      if (team.getStudents().stream().flatMap(x -> x.getTeams().stream().filter(y -> y.getCourse().getId() == team.getCourse().getId())
          .filter(z -> z.isActive())).count() > 0)
        throw new StudentInMultipleActiveTeamsException();
      if (team.isDisabled() == false) {
        team.setActive(true); // no need to save, will be flushed automatically at the end of transaction (since not a new entity)
        team.getStudents().stream().flatMap(x -> x.getTeams().stream().filter(y -> y.getCourse().getId() == team.getCourse().getId())
            .filter(z -> !z.isActive())).forEach(t -> t.setDisabled(true));
      }
    }
    return true; // token accepted
  }

  /**
   * {@link it.polito.ai.es2.controllers.Notification_Controller#rejectTokenTeam(String)}
   */
  @Override
  public boolean rejectTeam(@NotBlank String idtoken) {
    Optional<Team> optionalTeam = tokenRepository.findById(idtoken).map(Token::getTeam);
    if (optionalTeam.isEmpty())
      return false;
    tokenRepository.deleteAll(optionalTeam.get().getTokens());
    notificationService.cleanUpOldTokens();
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