package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.services.interfaces.CourseService;
import it.polito.ai.es2.services.interfaces.ImageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/API/images")
@Log
@Validated
public class APIImages_RestController {
  @Autowired
  ImageService imageService;
  @Autowired
  private CourseService courseService;

  //
  @PostMapping()
  public ResponseEntity<String> uploadImage(@RequestParam("imageFile") @NotNull MultipartFile file) {
    imageService.uploadImage(file);
    return ResponseEntity.status(HttpStatus.OK).body("{\"Response\": \"Upload successful!\"}");
  }

  @GetMapping(path = {"/{imageId}"})
  public @ResponseBody
  ImageDTO getImage(@PathVariable("imageId") @NotNull Long imageId) {
    return imageService.getImage(imageId);
  }
}
