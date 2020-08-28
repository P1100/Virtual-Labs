package it.polito.ai.es2.services;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.entities.Image;
import it.polito.ai.es2.repositories.*;
import it.polito.ai.es2.services.exceptions.ImageException;
import it.polito.ai.es2.services.exceptions.ImageNotFoundException;
import it.polito.ai.es2.services.exceptions.VlException;
import it.polito.ai.es2.services.interfaces.ImageService;
import it.polito.ai.es2.services.interfaces.NotificationService;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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

  /**
   * POST {@link it.polito.ai.es2.controllers.APIImages_RestController#uploadImage(MultipartFile)}
   */
  @Override
  public void uploadImage(MultipartFile file) {
    if (file == null || file.isEmpty())
      throw new ImageException("null file, or empty");
    Image img = new Image();
    img.setName(file.getOriginalFilename());
    img.setType(file.getContentType());
    try {
      img.setPicByte(compressBytes(file.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
      throw new VlException("IOException image file");
    }
    imageRepository.save(img);
  }

  /**
   * GET {@link it.polito.ai.es2.controllers.APIImages_RestController#getImage(Long)}
   */
  @Override
  public ImageDTO getImage(Long imageId) {
    Optional<Image> retrievedImage = imageRepository.findById(imageId);
    if (retrievedImage.isEmpty())
      throw new ImageNotFoundException(imageId.toString());
    ImageDTO img = new ImageDTO();
    img.setName(retrievedImage.get().getName());
    img.setType(retrievedImage.get().getType());
    byte[] bytes = decompressBytes(retrievedImage.get().getPicByte());
    img.setImageStringBase64(Base64.getEncoder().encodeToString(bytes));
    return img;
  }

  // Compress the image bytes before storing it in the database
  private byte[] compressBytes(byte[] data) {
    System.out.println("Original Image Byte Size - " + data.length);
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    while (!deflater.finished()) {
      int count = deflater.deflate(buffer);
      outputStream.write(buffer, 0, count);
    }
    try {
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new ImageException("IOException");
    }
    System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
    return outputStream.toByteArray();
  }

  // Uncompress the image bytes before returning it to the angular application
  private byte[] decompressBytes(byte[] data) {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(buffer);
        outputStream.write(buffer, 0, count);
      }
      outputStream.close();
    } catch (IOException | DataFormatException e) {
      e.printStackTrace();
      throw new ImageException("IOException | DataFormatException");
    }
    byte[] decompressed = outputStream.toByteArray();
    log.info("Original: " + data.length + " bytes. " + "Decompressed: " + decompressed.length + " bytes.");
    return decompressed;
  }
}
