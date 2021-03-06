package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.VmDTO;
import it.polito.ai.es2.services.interfaces.VLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/vms")
@Validated
public class APIVms_RestController {
  @Autowired
  private VLService vlService;

  @PostMapping
  public void createVm(@Valid @RequestBody VmDTO vm) {
    vlService.createVm(vm);
  }
  @PutMapping
  public void editVm(@Valid @RequestBody VmDTO vm) {
    vlService.editVm(vm);
  }

  @GetMapping
  public void getAllVm(@Valid @RequestBody VmDTO vm) {
  }

  @GetMapping("/{teamId}")
  public List<VmDTO> getTeamVms(@PathVariable @NotNull Long teamId) {
    return vlService.getTeamVms(teamId);
  }

  @PutMapping("/vm/{vmId}/enable")
  public void enableVm(@PathVariable @NotNull Long vmId) {
    vlService.changeStatusVm(vmId, true);
  }

  @PutMapping("/vm/{vmId}/disable")
  public void disableVm(@PathVariable @NotNull Long vmId) {
    vlService.changeStatusVm(vmId, false);
  }

  @DeleteMapping("/vm/{vmId}")
  public void deleteVm(@PathVariable @NotNull Long vmId) {
    vlService.deleteVm(vmId);
  }
}
