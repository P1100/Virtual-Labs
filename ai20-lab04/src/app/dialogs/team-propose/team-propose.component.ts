import {Component, Inject} from '@angular/core';
import {AppSettings} from '../../app-settings';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {AlertsService} from '../../services/alerts.service';
import {CourseService} from '../../services/course.service';
import {Router} from '@angular/router';
import {VlServiceService} from '../../services/vl-service.service';
import {dialogProposalData} from '../../r2-inner-tab/student/teams/teams.component';
import {Student} from '../../models/student.model';

@Component({
  selector: 'app-team-propose',
  templateUrl: './team-propose.component.html',
  styles: ['mat-form-field {width: 100%}']
})
export class TeamProposeComponent {
  checkboxNoValidate = false;
  showCheckboxNoValidateForTesting = AppSettings.devtest;
  nameTeam: string;
  hoursTimeoutTeam: number;
  url: string;

  constructor(private courseService: CourseService, private dialogRef: MatDialogRef<TeamProposeComponent>,
              @Inject(MAT_DIALOG_DATA) public data: dialogProposalData, private router: Router, private alertsService: AlertsService,
              private vlServiceService: VlServiceService) {
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    const memberIds = [...this.data.membersWithProposerFirst.map(x => x.id)];
    this.vlServiceService.proposeTeam(this.data.courseId, this.nameTeam, this.hoursTimeoutTeam, memberIds).subscribe(
      x => {
        this.dialogRef.close('success');
        this.router.navigateByUrl(this.data.url);
        this.alertsService.setAlert('success', 'Proposal sent!');
      },
      e => {
        this.alertsService.setAlert('danger', 'Couldn\'t submit proposal.' + e);
        this.dialogRef.close();
      }
    );
  }
}
