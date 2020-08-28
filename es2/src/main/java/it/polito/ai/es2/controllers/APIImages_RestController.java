package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.services.interfaces.ImageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "API/images")
@Log
public class APIImages_RestController {
  @Autowired
  ImageService imageService;

  @PostMapping(value = "/")
  public ResponseEntity<String> uploadImage(@RequestParam("imageFile") MultipartFile file) {
    if (file == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "null file");
    imageService.uploadImage(file);
    return ResponseEntity.status(HttpStatus.OK).body("{\"Response\": \"Upload successful!\"}");
  }

  @GetMapping(path = {"/{imageId}"})
  public @ResponseBody
  ImageDTO getImage(@PathVariable("imageId") Long imageId) {
    return imageService.getImage(imageId);
  }
}
