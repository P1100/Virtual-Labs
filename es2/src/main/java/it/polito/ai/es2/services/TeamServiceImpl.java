package it.polito.ai.es2.services;

import it.polito.ai.es2.controllers.APITeams_RestController;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.services.exceptions.*;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.TeamService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  /**
   * GET {@link it.polito.ai.es2.controllers.APITeams_RestController#getMembers(Long)}
   */
  @Override
  public List<StudentDTO> getMembers(Long teamId) {
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
  public Optional<TeamDTO> getTeam(Long teamId) {
    log.info("getTeam(" + teamId + ")");
    if (teamId == null) throw new NullParameterException("null team parameter");
    return teamRepository.findById(teamId).map(x -> modelMapper.map(x, TeamDTO.class));
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#proposeTeam(String, String, List)}
   */
  @Override
  public TeamDTO proposeTeam(String courseName, String team_name, List<Long> memberIds) {
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
    notificationService.notifyTeam(return_teamDTO, memberIds);
    return return_teamDTO;
  }

  /**
   * {@link it.polito.ai.es2.controllers.APITeams_RestController#evictTeam(Long)}
   */
  @Override
  public boolean evictTeam(Long teamId) {
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