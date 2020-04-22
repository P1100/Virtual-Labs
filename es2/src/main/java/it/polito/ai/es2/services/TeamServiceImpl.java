package it.polito.ai.es2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.es2.domains.StudentViewModel;
import it.polito.ai.es2.dtos.CourseDTO;
import it.polito.ai.es2.dtos.StudentDTO;
import it.polito.ai.es2.dtos.TeamDTO;
import it.polito.ai.es2.entities.Course;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.repositories.CourseRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.services.exceptions.CourseNotFoundException;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamServiceException;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: Controlli su presenza valore nel repository, da 2 istruzione (repo.existsID+repo.getOne) a <Optional> repo.findById() !
@Service
@Transactional
@Log
public class TeamServiceImpl implements TeamService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CourseRepository cr;
    @Autowired
    StudentRepository sr;
    @Autowired
    TeamRepository tr;

    @Override
    public Optional<CourseDTO> getCourse(String name) {
        return cr.findById(name).map(x -> modelMapper.map(x, CourseDTO.class));
    }

    @Override
    public boolean addStudent(StudentDTO student) {
        if (student == null || student.getId() == null)
            return false;
        Student s = modelMapper.map(student, Student.class);
        try {
            if (!sr.existsById(student.getId())) {
                sr.save(s);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            log.warning("###### IllegalArgumentException:" + e.toString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.warning("###### Other Exception:" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addCourse(CourseDTO course) {
        if (course == null || course.getName() == null) {
            return false;
        }
        Course c = modelMapper.map(course, Course.class);
        try {
            if (!cr.existsById(course.getName())) {
                cr.save(c);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            log.warning("IllegalArgumentException:" + e.toString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.warning("###### Other Exception:" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return cr.findAll().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getCourses(String studentId) {
        if (studentId == null) {
            throw new TeamServiceException("null parameters");
        }
        return sr.getOne(studentId).getCourses().stream().map(x -> modelMapper.map(x, CourseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDTO> getStudent(String studentId) {
        return sr.findById(studentId).map(x -> modelMapper.map(x, StudentDTO.class));
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return sr.findAll().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getEnrolledStudents(String courseName) throws CourseNotFoundException {
        if (courseName == null)
            throw new TeamServiceException("null parameters");
        if (!cr.existsById(courseName))
            throw new CourseNotFoundException("StudentNotFoundException: getEnrolledStudents(" + courseName + ")");
        Course c = cr.getOne(courseName);
        List<StudentDTO> returnListDTO = c.getStudents().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
        return returnListDTO;
    }

    @Override
    public void enableCourse(String courseName) throws CourseNotFoundException {
        if (courseName == null)
            throw new CourseNotFoundException("null parameter");
        if (!cr.existsById(courseName))
            throw new CourseNotFoundException("course not found");
        cr.getOne(courseName).setEnabled(true);
        cr.flush();
    }

    @Override
    public void disableCourse(String courseName) throws CourseNotFoundException {
        if (courseName == null)
            throw new CourseNotFoundException("null parameter");
        if (!cr.existsById(courseName))
            throw new CourseNotFoundException("course not found");
        cr.getOne(courseName).setEnabled(false);
        cr.flush();
    }

    @Override
    public boolean addStudentToCourse(String studentId, String courseName) throws StudentNotFoundException, CourseNotFoundException {
        log.info("##### loading addStudentToCourse(" + studentId + "," + courseName + ")" + " - Initial checks + repos.getOne####");
        if (studentId == null || courseName == null)
            throw new TeamServiceException("null parameters");
        // Nota: più ottimizzato fare repo.findById(…) e trattare il risultato come Optional! Si fà una sola query così
        if (!sr.existsById(studentId))
            throw new StudentNotFoundException("student not found");
        Optional<Course> courseOptional = cr.findById(courseName);
        if (!courseOptional.isPresent())
            throw new CourseNotFoundException("course not found");
        Course c = courseOptional.get();
        if (!c.isEnabled())
            throw new TeamServiceException("course not enabled, cannot add student");

        Student s = sr.getOne(studentId);
        if (c.getStudents().stream().anyMatch(x -> x.getId().equals(studentId)))
            return false;
        log.info("[before repo add]-addStudentToCourse(" + studentId + "," + courseName + ")");
        c.addStudent(s);
        // TODO controllare che save non serva
        /* Flush e save non servono!(?) Essendo una transazione vengono effettuati automaticamente al termine del metodo! */
        log.info("[after repo add]-addStudentToCourse(" + studentId + "," + courseName + ").Course:" + c.getName() + "; Student:" + s.getId());
        return true;
    }
    @Override
    public List<Boolean> addAll(List<StudentDTO> students) {
        if (students == null) {
            throw new TeamServiceException("null parameters");
        }
        List<Boolean> lb = students.stream().map(x -> modelMapper.map(x, Student.class))
                .peek(e -> log.info("addAll(List<StudentDTO> - size:" + students.size() + " - Entità corrente:" + e)).map(
                        e -> {
                            boolean b = sr.existsById(e.getId());
                            // Se dato esisteva già...
                            if (b)
                                return Boolean.FALSE;
                            // Se invece non esisteva...
                            sr.save(e);
                            return Boolean.TRUE;
                        }).collect(Collectors.toList());
        sr.flush();
        return lb;
    }
    @Override
    public List<Boolean> enrollAll(List<String> studentIds, String courseName) {
        if (studentIds == null || courseName == null) {
            throw new TeamServiceException("enrollAll(List<String> studentIds, String courseName) - null parameters");
        }
        if (!cr.existsById(courseName))
            throw new CourseNotFoundException("enrollAll(List<String> studentIds, String courseName) - course not found");
        Course course = cr.getOne(courseName);
        List<Boolean> lb = new ArrayList<>();
        for (String id : studentIds) {
            if (!sr.existsById(id))
                throw new StudentNotFoundException("enrollAll(List<String> studentIds, String courseName) - student in list not found");
            Student student = sr.getOne(id);
            if (course.getStudents().stream().anyMatch(x -> x.getId().equals(id))) {
                lb.add(false);
                continue;
            }
            lb.add(true);
            course.addStudent(student);
            student.addCourse(course);
        }
        return lb;
    }
    @Override
    public List<Boolean> addAndEroll(Reader reader, String courseName) {
        if (reader == null || courseName == null) {
            throw new TeamServiceException("addAndEroll(Reader reader, String courseName) - null parameters");
        }
        Optional<Course> courseOptional = cr.findById(courseName);
        if (!courseOptional.isPresent())
            throw new CourseNotFoundException("addAndEroll(Reader reader, String courseName) - course not found");
        CsvToBean<StudentViewModel> csvToBean = new CsvToBeanBuilder(reader)
                .withType(StudentViewModel.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<StudentViewModel> users = csvToBean.parse();
        log.info(String.valueOf(users));

        List<String> list_ids = users.stream()
                .map(x -> modelMapper.map(x, StudentDTO.class))
                .map(s ->
                {
                    Optional<Student> os = sr.findById(s.getId());
                    if (os.isPresent())
                        return os.get();
                    Student newStudent = modelMapper.map(s, Student.class);
                    return sr.save(newStudent);
                })
//                .filter(p -> p.isPresent() ? true : sr.save(p.get())!=null)
//                .peek(e -> System.out.println(".map(s -> sr.findById(s.getId())) - Mapped value: " + e))
//                .map(p -> p.isPresent() ? p.get() : sr.save(new Student()))
//                .peek(e -> System.out.println(".map(p -> p.isPresent() ? p.get() : sr.save(p.get())) - Mapped value: " + e))
                .map(y -> y.getId()).collect(Collectors.toList());

        log.info(list_ids + "-" + courseName);
        return enrollAll(list_ids, courseName);
    }

    @Override
    public List<TeamDTO> getTeamsForStudent(String studentId) {
        return sr.getOne(studentId).getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getMembers(Long teamId) {
        return tr.getOne(teamId).getMembers().stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
    }

    @Override
    public TeamDTO proposeTeam(String courseId, String name, List<String> memberIds) throws TeamServiceException {
        if (courseId == null || name == null || memberIds == null)
            throw new TeamServiceException("null parameter");
        // Si dà per assunto che il team non sia stato già creato
        Optional<Course> oc = cr.findById(courseId);
        if (!oc.isPresent())
            throw new CourseNotFoundException("courseId not found");
        if (oc.get().isEnabled() == false)
            throw new TeamServiceException("proposeTeam() - course not enabled");
        Course course = oc.get();

        List<Optional<Student>> streamopt = memberIds.stream().map(x -> sr.findById(x)).collect(Collectors.toList());
        if (!streamopt.stream().allMatch(x -> x.isPresent()))
            throw new StudentNotFoundException("courseId not found");
        List<Student> listMemberStudents = streamopt.stream().map(x -> x.get()).collect(Collectors.toList());

        if (!listMemberStudents.stream().allMatch(x -> course.getStudents().contains(x)))
            throw new TeamServiceException("proposeTeam() - non tutti gli studenti sono iscritti al corso");
        if (course.getTeams().size() != 0 && course.getTeams().stream().map(x -> x.getMembers()).noneMatch(y -> {
            for (Student student : y) {
                if (listMemberStudents.stream().anyMatch(student::equals))
                    return true;
            }
            return false;
        }))
            throw new TeamServiceException("proposeTeam() - studenti fanno parte di altri gruppi nell’ambito dello stesso corso");
        if (listMemberStudents.size() < course.getMin() || listMemberStudents.size() > course.getMax())
            throw new TeamServiceException("proposeTeam() - non rispettati i vincoli di cardinalità definiti nell’ambito del corso");
        if (!listMemberStudents.stream().allMatch(new HashSet<>()::add))
            throw new TeamServiceException("proposeTeam() - duplicati nell'elenco dei partecipanti");
        TeamDTO teamDTO = new TeamDTO(name, 1);
        Team team = modelMapper.map(teamDTO, Team.class);
        for (Student x : listMemberStudents.stream().collect(Collectors.toList())) {
            team.addStudent(x);
        }
        course.addTeam(team);
        team = tr.save(team);
        return teamDTO;
    }

    @Override
    public List<TeamDTO> getTeamForCourse(String courseName) {
        if (courseName == null)
            throw new TeamServiceException("null parameter");
        Optional<Course> co = cr.findById(courseName);
        if (!co.isPresent())
            throw new CourseNotFoundException("getTeamForCourse - course not found");
        return co.get().getTeams().stream().map(x -> modelMapper.map(x, TeamDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getStudentsInTeams(String courseName) {
        return cr.getStudentsInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getAvailableStudents(String courseName) {
        return cr.getStudentsNotInTeams(courseName).stream().map(x -> modelMapper.map(x, StudentDTO.class)).collect(Collectors.toList());
    }
}
