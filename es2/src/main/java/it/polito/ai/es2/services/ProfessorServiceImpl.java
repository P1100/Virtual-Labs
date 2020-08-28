package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.ProfessorDTO;
import it.polito.ai.es2.repositories.ProfessorRepository;
import it.polito.ai.es2.services.interfaces.ProfessorService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log
public class ProfessorServiceImpl implements ProfessorService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  ProfessorRepository professorRepository;

  @Override
  public boolean addProfessor(ProfessorDTO professor) {
//    log.info("addProfessor(" + professor + ")");
//    if (professor == null || professor.getId() == null) return false;
//    Professor p = modelMapper.map(professor, Professor.class);
//    try {
//      if (!professorRepository.existsById(professor.getId())) {
//        professorRepository.save(p);
//        return true;
//      }
//      return false;
//    } catch (IllegalArgumentException e) {
//      log.warning("###### IllegalArgumentException:" + e);
//      e.printStackTrace();
//      return false;
//    } catch (Exception e) {
//      log.warning("###### Other Exception:" + e);
//      e.printStackTrace();
    return false;
//    }
  }
}
