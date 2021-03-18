package it.polito.ai.es2;

import it.polito.ai.es2.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
  @Autowired
  private TeamService teamService;

  @Scheduled(cron = "0 0 0 25 12 ?")
  public void reportCurrentTime() {
    teamService.cleanupTeamsExpiredDisabled(null);
  }
}