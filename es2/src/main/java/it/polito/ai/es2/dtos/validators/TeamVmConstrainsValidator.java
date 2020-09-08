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
    int maxVcpu = 0;
    int maxDisk = 0;
    int maxRam = 0;
    int maxRunningVM = 0;
    int maxTotVM = 0;
    for (VM v : vm.getTeam().getVms()) {
      maxVcpu += v.getVcpu();
      maxRam += v.getRam();
      maxDisk += v.getDisk();
      maxRunningVM += v.isActive() ? 1 : 0;
      maxTotVM += 1;
    }
    StringBuilder sb = new StringBuilder();
    if (maxVcpu > t.getMaxVcpu()) {
      sb.append(maxVcpu + " is bigger than team max vcpu " + t.getMaxVcpu() + " \n");
    }
    if (maxDisk > t.getMaxDisk()) {
      sb.append(maxDisk + " is bigger than team max disk " + t.getMaxDisk() + " \n");
    }
    if (maxRam > t.getMaxRam()) {
      sb.append(maxRam + " is bigger than team max ram " + t.getMaxRam() + " \n");
    }
    if (maxRunningVM > t.getMaxRunningVM()) {
      sb.append(maxRunningVM + " is bigger than team max running vm " + t.getMaxRunningVM() + " \n");
    }
    if (maxTotVM > t.getMaxTotVM()) {
      sb.append(maxTotVM + " is bigger than team max tot vm " + t.getMaxTotVM() + " \n");
    }
    if (sb.length() > 0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(sb.toString()).addConstraintViolation();
      return false;
    }
    return true;
  }
}