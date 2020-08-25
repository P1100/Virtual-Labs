package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.ImageDTO;
import it.polito.ai.es2.entities.Image;
import it.polito.ai.es2.repositories.ImageRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
@RestController
@RequestMapping(path = "API/images")
@Log
public class APIImages_RestController {
  @Autowired
  ImageRepository imageRepository;
  
  @Transactional
  @PostMapping(value = "/upload")
  public ResponseEntity<String> uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
    System.out.println("Original Image Byte Size - " + file.getBytes().length);
    Image img = new Image();
    img.setName(file.getOriginalFilename());
    img.setType(file.getContentType());
    img.setPicByte(compressBytes(file.getBytes()));
    Image save = imageRepository.save(img);
    return ResponseEntity.status(HttpStatus.OK).body("{\"Response\": \"Upload successful!\"}");
  }
  @Transactional
  @GetMapping(path = { "/get/{imageId}" })
  public @ResponseBody ImageDTO getImage(@PathVariable("imageId") Long imageId) throws IOException {
    final Optional<Image> retrievedImage = imageRepository.findById(imageId);
    if (retrievedImage.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    ImageDTO img = new ImageDTO(); img.setName(retrievedImage.get().getName());
    img.setType(retrievedImage.get().getType());
    byte[] bytes = decompressBytes(retrievedImage.get().getPicByte());
    img.setImageStringBase64(Base64.getEncoder().encodeToString(bytes));
    return img;
  }
  // compress the image bytes before storing it in the database
  public static byte[] compressBytes(byte[] data) {
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
    }
    System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
    return outputStream.toByteArray();
  }
  // uncompress the image bytes before returning it to the angular application
  public static byte[] decompressBytes(byte[] data)  {
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
    } catch (IOException ioe) {
      System.out.println(ioe);
    } catch (DataFormatException e) {
      System.out.println(e);
    }
    byte[] decompressed = outputStream.toByteArray();
    log.info("Original: " + data.length + " bytes. " + "Decompressed: " + decompressed.length + " bytes.");
    return decompressed;
  }
}