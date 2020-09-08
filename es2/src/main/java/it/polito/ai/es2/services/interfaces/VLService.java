package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.VmDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface VLService {
  @PreAuthorize("hasRole('STUDENT')")
  void createVm(@Valid VmDTO vmDTO);

  List<VmDTO> getTeamVm(@NotNull Long teamId);

  void changeStatusVm(@NotNull Long vmId, boolean newStatus);
}
