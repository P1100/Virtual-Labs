package it.polito.ai.es2.dtos.validators;

import it.polito.ai.es2.entities.Team;
import it.polito.ai.es2.entities.VM;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TeamVmConstrainsValidator implements ConstraintValidator<TeamVmConstrains, VM> {
  @Override
  public void initialize(TeamVmConstrains constraintAnnotation) {
  }

  @Override
  public boolean isValid(VM vm, ConstraintValidatorContext context) {
    Team t = vm.getTeam();
    if (t == null)
      return true;
    int countVcput = 0;
    int countDisk = 0;
    int countRam = 0;
    int countRunningVM = 0;
    int countTotVM = 0;
    for (VM v : vm.getTeam().getVms()) {
      countVcput += v.getVcpu();
      countRam += v.getRam();
      countDisk += v.getDisk();
      countRunningVM += v.isActive() ? 1 : 0;
      countTotVM += 1;
    }
    StringBuilder sb = new StringBuilder();
    if (countVcput > t.getMaxVcpu()) {
      sb.append(countVcput + " is bigger than team max vcpu " + t.getMaxVcpu() + " \n");
    }
    if (countDisk > t.getMaxDisk()) {
      sb.append(countDisk + " is bigger than team max disk " + t.getMaxDisk() + " \n");
    }
    if (countRam > t.getMaxRam()) {
      sb.append(countRam + " is bigger than team max ram " + t.getMaxRam() + " \n");
    }
    if (countRunningVM > t.getMaxRunningVm()) {
      sb.append("Max running VM limit reached: " + countRunningVM + "/" + t.getMaxRunningVm() + " \n");
    }
    if (countTotVM > t.getMaxTotVm()) {
      sb.append("Max VM limit reached: " + countTotVM + "/" + t.getMaxTotVm() + " \n");
    }
    if (sb.length() > 0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(sb.toString()).addConstraintViolation();
      return false;
    }
    return true;
  }
}