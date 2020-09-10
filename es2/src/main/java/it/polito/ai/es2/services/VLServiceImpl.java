package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.AssignmentDTO;
import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.dtos.ImplementationDTO;
import it.polito.ai.es2.dtos.VmDTO;
import it.polito.ai.es2.entities.Image;
import it.polito.ai.es2.entities.*;
import it.polito.ai.es2.repositories.*;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamNotFoundException;
import it.polito.ai.es2.services.exceptions.VmNotFoundException;
import it.polito.ai.es2.services.interfaces.ImageService;
import it.polito.ai.es2.services.interfaces.VLService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.AttributedString;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Validated
@Log
public class VLServiceImpl implements VLService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  VMRepository vmRepository;
  @Autowired
  ImageService imageService;
  @Autowired
  ImageRepository imageRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  AssignmentRepository assignmentRepository;
  @Autowired
  ImplementationRepository implementationRepository;
  @Autowired
  Validator validator;
  static String FILEPATH = "src/main/resources/test"; // Path of temp file
  static File file = new File(FILEPATH);

  @Override
  @PreAuthorize("hasRole('STUDENT')")
  public void createVm(@Valid VmDTO vmDTO) {
    log.info("createVm(" + vmDTO + ", " + vmDTO.getTeamId() + ", " + vmDTO.getStudentCreatorId() + ")");
    VM vm = new VM();
    vm.setActive(false);
    vm.setVcpu(vmDTO.getVcpu());
    vm.setDisk(vmDTO.getDisk());
    vm.setRam(vmDTO.getRam());

    Optional<Student> studentOptional = studentRepository.findById(vmDTO.getStudentCreatorId());
    if (studentOptional.isEmpty())
      throw new StudentNotFoundException(vmDTO.getStudentCreatorId().toString());
    Optional<Team> teamOptional = teamRepository.findById(vmDTO.getTeamId());
    if (teamOptional.isEmpty())
      throw new TeamNotFoundException(vmDTO.getTeamId());
    vm.addSetCreator(studentOptional.get());
    vm.addSetTeam(teamOptional.get());

    Set<ConstraintViolation<VM>> constraintViolations = validator.validate(vm);
    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(constraintViolations);
    }

    ImageDTO imageDTO = null;
    Path path = Paths.get("src/main/resources/vm.jpeg");
    String name = "vm.jpeg";
    String originalFileName = "vm.jpeg";
    String contentType = "image/jpeg";
    byte[] content = null;
    content = addTimestampImage(path, content);
    MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);
    imageDTO = imageService.uploadImage(multipartFile);
    if (imageDTO == null || imageDTO.getId() == null)
      throw new RuntimeException("Critical server error: upload failed silently)");
    Image image = imageRepository.findById(imageDTO.getId()).orElse(null);
    if (image == null)
      throw new RuntimeException("Critical server error: image was not saved)");
    vm.addSetImage(image);

    vmRepository.save(vm);
  }

  private byte[] addTimestampImage(Path path, byte[] content) {
    try {
      content = Files.readAllBytes(path);
      OutputStream os = new FileOutputStream(file);
      os.write(content);
      os.close();
//      Files.write(new File(FILEPATH).toPath(), content);
      BufferedImage image = ImageIO.read(new File(FILEPATH));
      Font font = new Font("Arial", Font.BOLD, 18);
      Graphics g = image.getGraphics();
      g.setFont(font);
      g.setColor(Color.GREEN);
      String text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      AttributedString attributedText = new AttributedString(text);
      attributedText.addAttribute(TextAttribute.FONT, font);
      attributedText.addAttribute(TextAttribute.FOREGROUND, Color.GREEN);
      FontMetrics metrics = g.getFontMetrics(font);
      int positionX = (image.getWidth() - metrics.stringWidth(text));
      int positionY = (image.getHeight() - metrics.getHeight()) + metrics.getAscent();
      g.drawString(attributedText.getIterator(), positionX, positionY);
//      BufferedImage originalImage = ImageIO.read(new File("c:\\image\\mypic.jpg"));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", baos);
      baos.flush();
      content = baos.toByteArray();
      baos.close();
    } catch (IOException e) {
    }
    return content;
  }

  @PreAuthorize("hasRole('STUDENT') or hasRole('PROFESSOR')")
  @Override public List<VmDTO> getTeamVms(@NotNull Long teamId) {
    Optional<Team> teamOptional = teamRepository.findById(teamId);
    if (teamOptional.isEmpty())
      throw new TeamNotFoundException(teamId);
    Team t = teamOptional.get();
    List<VmDTO> vmDTOS = modelMapper.map(t.getVms(), new TypeToken<List<VmDTO>>() {}.getType());
    return vmDTOS;
  }

  @PreAuthorize("hasRole('STUDENT') and @mySecurityChecker.isVmOwner(#vmId,authentication.principal.username)")
  @Override public void changeStatusVm(@NotNull Long vmId, boolean newStatus) {
    vmRepository.findById(vmId).map(vm -> {
      vm.setActive(newStatus);
      Set<ConstraintViolation<VM>> constraintViolations = validator.validate(vm);
      if (!constraintViolations.isEmpty()) {
        Set<String> messages = new HashSet<>(constraintViolations.size());
        throw new ConstraintViolationException(constraintViolations);
      }
      return true;
    }).orElseThrow(() -> new VmNotFoundException(vmId));
    return;
  }
  @PreAuthorize("hasRole('STUDENT') and @mySecurityChecker.isVmOwner(#vmDTO,authentication.principal.username)")
  @Override public void editVm(@Valid VmDTO vmDTO) {
    VM vm = vmRepository.findById(vmDTO.getId()).orElseThrow(() -> new VmNotFoundException(vmDTO.getId()));
    vm.setVcpu(vmDTO.getVcpu());
    vm.setRam(vmDTO.getRam());
    vm.setDisk(vmDTO.getDisk());
    vmRepository.save(vm);
  }
  @PreAuthorize("hasRole('STUDENT') and @mySecurityChecker.isVmOwner(#vmId,authentication.principal.username)")
  @Override public void deleteVm(@NotNull Long vmId) {
    VM vm = vmRepository.findById(vmId).orElseThrow(() -> new VmNotFoundException(vmId));
    for (Student sharedOwner : vm.getSharedOwners()) {
      sharedOwner.getVmsCreated().remove(vm);
      sharedOwner.getVmsOwned().remove(vm);
    }
    vm.getCreator().getVmsOwned().remove(vm);
    vm.getCreator().getVmsCreated().remove(vm);
    vm.getTeam().getVms().remove(vm);
    // image handled by remove cascade
    vmRepository.deleteById(vm.getId());
  }

  @PreAuthorize("hasRole('PROFESSOR')")
  @Override public List<AssignmentDTO> getAllAssignments(@NotNull String courseId) {
    List<Assignment> assignments = assignmentRepository.findAllByCourse_Id(courseId);
    return modelMapper.map(assignments, new TypeToken<List<AssignmentDTO>>() {
    }.getType());
  }

  @Override public void updateImplementation(ImplementationDTO dto) {
    Implementation implementation = implementationRepository.findById(dto.getId()).orElse(null);
    if (implementation == null)
      return;
    implementation.setCurrentCorrection(dto.getCurrentCorrection());
    implementation.setGrade(dto.getGrade());
    implementation.setPermanent(dto.getPermanent());
    implementationRepository.save(implementation);
  }
}
