package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.dtos.VmDTO;
import it.polito.ai.es2.entities.Image;
import it.polito.ai.es2.entities.Student;
import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.entities.VM;
import it.polito.ai.es2.repositories.ImageRepository;
import it.polito.ai.es2.repositories.StudentRepository;
import it.polito.ai.es2.repositories.TeamRepository;
import it.polito.ai.es2.repositories.VMRepository;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamNotFoundException;
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
import javax.validation.Valid;
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
import java.util.List;
import java.util.Optional;

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
  // Path of a file
  static String FILEPATH = "src/main/resources/test";
  static File file = new File(FILEPATH);

  @Override
  @PreAuthorize("hasRole('STUDENT')") // TODO: security checks
  public void createVm(@Valid VmDTO vmDTO) {
    log.info("createVm(" + vmDTO + ", " + vmDTO.getTeamId() + ", " + vmDTO.getStudentCreatorId() + ")");
    VM vm = new VM();
    vm.setActive(false);
    vm.setVcpu(vmDTO.getVcpu());
    vm.setDisk(vmDTO.getDisk());
    vm.setRam(vmDTO.getRam());

    ImageDTO imageDTO = null;
    Path path = Paths.get("src/main/resources/vm.jpeg");
    String name = "vm.jpeg";
    String originalFileName = "vm.jpeg";
    String contentType = "image/jpeg";
    byte[] content = null;
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
    MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);
    imageDTO = imageService.uploadImage(multipartFile);

    if (imageDTO == null || imageDTO.getId() == null) throw new RuntimeException("Critical server error: upload failed silently)");
    Image image = imageRepository.findById(imageDTO.getId()).orElse(null);
    if (image == null) throw new RuntimeException("Critical server error: image was not saved)");
    vm.addSetImage(image);
    Optional<Student> studentOptional = studentRepository.findById(vmDTO.getStudentCreatorId());
    if (studentOptional.isEmpty()) throw new StudentNotFoundException(vmDTO.getStudentCreatorId().toString());
    Optional<Team> teamOptional = teamRepository.findById(vmDTO.getTeamId());
    if (teamOptional.isEmpty()) throw new TeamNotFoundException(vmDTO.getTeamId());
    vm.addSetCreator(studentOptional.get());
    vm.addSetTeam(teamOptional.get());
    vmRepository.save(vm);
  }

  @Override
  public List<VmDTO> getTeamVm(@NotNull Long teamId) {
    Optional<Team> teamOptional = teamRepository.findById(teamId);
    if (teamOptional.isEmpty())
      throw new TeamNotFoundException(teamId);
    Team t = teamOptional.get();
    List<VmDTO> vmDTOS = modelMapper.map(t.getVms(), new TypeToken<List<VmDTO>>() {
    }.getType());
    return vmDTOS;
  }
}
