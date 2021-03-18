package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public interface ImageService {
  ImageDTO uploadImage(@NotNull MultipartFile file);

  ImageDTO getImage(@NotNull Long imageId);

  byte[] getBytesImage(@NotNull Long imageId);
}
