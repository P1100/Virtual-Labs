package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.services.interfaces.CourseService;
import it.polito.ai.es2.services.interfaces.ImageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/api/images")
@Log
@Validated
public class APIImages_RestController {
  @Autowired
  ImageService imageService;
  @Autowired
  private CourseService courseService;

  @PostMapping()
  public ImageDTO uploadImage(@RequestParam("imageFile") @NotNull MultipartFile file) {
    return imageService.uploadImage(file);
//    return ResponseEntity.status(HttpStatus.OK).body("{\"Response\": \"Upload successful!\"}");
  }

  @GetMapping(path = {"/{imageId}"})
  public ImageDTO getImage(@PathVariable("imageId") @NotNull Long imageId) {
    return imageService.getImage(imageId);
  }
}
