package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.services.interfaces.CourseService;
import it.polito.ai.es2.services.interfaces.ImageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/images")
@Log
@Validated
public class APIImages_RestController {
  @Autowired
  ImageService imageService;
  @Autowired
  private CourseService courseService;
  @Autowired
  ServletContext servletContext;

  @PostMapping()
  public ImageDTO uploadImage(@RequestParam("imageFile") @NotNull MultipartFile file) {
    return imageService.uploadImage(file);
  }

  @GetMapping(path = {"/{imageId}"})
  public ImageDTO getImage(@PathVariable("imageId") @NotNull Long imageId) {
    return imageService.getImage(imageId);
  }

  // Example: http://localhost:8080/api/images/direct/1
  @GetMapping
  @RequestMapping(value = "/direct/{imageId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> getDirectLinkImage(@PathVariable Long imageId) throws IOException {
    byte[] bytes = imageService.getBytesImage(Long.valueOf(imageId));
    return ResponseEntity
        .ok()
        .contentType(MediaType.IMAGE_JPEG)
        .body(bytes);
  }
}
