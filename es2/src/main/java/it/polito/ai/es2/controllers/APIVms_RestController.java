package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.VmDTO;
import it.polito.ai.es2.services.interfaces.VLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vms")
public class APIVms_RestController {
  @Autowired
  private VLService vlService;

  @PostMapping
  public void createVm(@Valid @RequestBody VmDTO vm) {
    vlService.createVm(vm);
  }

  @GetMapping
  public void getAllVm(@Valid @RequestBody VmDTO vm) {
  }

  @GetMapping("/{teamId}")
  public List<VmDTO> getTeamVm(@PathVariable Long teamId) {
    return vlService.getTeamVm(teamId);
  }
}
