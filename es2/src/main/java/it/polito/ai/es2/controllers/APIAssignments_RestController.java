package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.AssignmentDTO;
import it.polito.ai.es2.dtos.ImplementationDTO;
import it.polito.ai.es2.services.interfaces.VLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@Validated
public class APIAssignments_RestController {
  @Autowired
  private VLService vlService;

  @PostMapping("/{courseId}") // Role professor, idProf da auth, image preload
  public void createAssignment(@PathVariable @NotBlank String courseId, @PathVariable @NotNull Long imageId) {
  }

  @GetMapping("/{courseId}")
  public List<AssignmentDTO> getAllAssignments(@PathVariable @NotBlank String courseId) {
    return vlService.getAllAssignments(courseId);
  }

  @PutMapping("/implementation")
  public void updateImplementation(@RequestBody @Valid ImplementationDTO implementationDTO) {
    vlService.updateImplementation(implementationDTO);
  }
}
