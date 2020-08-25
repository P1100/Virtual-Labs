package it.polito.ai.es2.services;

import it.polito.ai.es2.repositories.*;
import it.polito.ai.es2.services.interfaces.NotificationService;
import it.polito.ai.es2.services.interfaces.ProjectVLService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log
public class ProjectVLServiceImpl implements ProjectVLService {
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
  
  @Override
  public void testing(
  ) {
//    Image test = new Image();
//    test.setRevisionCycle(0);
//    imageRepository.saveAndFlush(test);
  }
}
