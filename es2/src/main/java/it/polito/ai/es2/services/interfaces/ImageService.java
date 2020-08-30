package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  ImageDTO uploadImage(MultipartFile file);

  ImageDTO getImage(Long imageId);
}
