package it.polito.ai.es2.controllers;

import it.polito.ai.es2.dtos.VmDTO;
import it.polito.ai.es2.services.interfaces.VLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/vms")
public class VmController {
  @Autowired
  private VLService vlService;

  @PostMapping
  public void createVm(@Valid @RequestBody VmDTO vm) {
    this.vlService.createVm(vm);
  }
}
