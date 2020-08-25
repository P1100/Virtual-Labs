package it.polito.ai.es2.services;

import it.polito.ai.es2.repositories.*;
import it.polito.ai.es2.services.interfaces.ImageService;
import it.polito.ai.es2.services.interfaces.NotificationService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Descrizione classe<p>Politica di sovrascrittura adottata: in quasi tutti i metodi add, se un id era già presente nel database non sovrascrivo i dati
 * già esistenti (tranne nel caso di proposeTeam, che poichè ha un id autogenerato, si è deciso di aggiornare il team vecchio usando
 * sempre la proposeTeam).
 */
@Service
@Transactional
@Log
public class ImageServiceImpl implements ImageService {
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
  @Autowired
  AssignmentRepository assignmentRepository;
  @Autowired
  ImageRepository imageRepository;
  @Autowired
  ImplementationRepository implementationRepository;
  @Autowired
  VMRepository vmRepository;
}
