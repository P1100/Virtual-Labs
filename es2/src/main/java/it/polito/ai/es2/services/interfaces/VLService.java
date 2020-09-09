package it.polito.ai.es2.services.interfaces;

import it.polito.ai.es2.dtos.VmDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface VLService {
  @PreAuthorize("hasRole('STUDENT')")
  void createVm(@Valid VmDTO vmDTO);

  List<VmDTO> getTeamVms(@NotNull Long teamId);

  void changeStatusVm(@NotNull Long vmId, boolean newStatus);

  @PreAuthorize("hasRole('STUDENT') and @mySecurityChecker.isVmOwner(#vmId,authentication.principal.username)") void editVm(@Valid VmDTO vmDTO);

  void deleteVm(@NotNull Long vmId);
}
