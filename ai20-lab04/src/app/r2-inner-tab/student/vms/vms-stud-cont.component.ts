import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';
import {Team} from '../../../models/team.model';
import {Subscription} from 'rxjs';
import {VlServiceService} from '../../../services/vl-service.service';
import {mergeMap} from 'rxjs/operators';
import {Vm} from '../../../models/vm.model';

@Component({
  selector: 'app-vms-stud-cont',
  template: `
    <app-vms-stud (forceUploadData)="onForceUploadData($event)"
                  [activeTeam]="activeTeam" [vms]="vms"
                  [idStringLoggedStudent]="idStringLoggedStudent"
                  (changeStatusVm)="changeStatusVm($event)"
                  (deleteVm)="deleteVm($event)"
    >
    </app-vms-stud>
  `,
  styleUrls: []
})
export class VmsStudContComponent implements OnDestroy {
  courseId = '0';
  subRouteParam: Subscription = null;
  idStringLoggedStudent: string;
  activeTeam: Team = null;
  vms: Vm[];

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService,
              private vlServiceService: VlServiceService) {
    this.idStringLoggedStudent = localStorage.getItem('id');
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        this.onForceUploadData(null);
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam?.unsubscribe();
  }
  onForceUploadData($event: any) {
    this.vlServiceService.getTeamsUser(+this.idStringLoggedStudent, this.courseId).pipe(
      mergeMap(teams => {
        let countActive = 0;
        this.activeTeam = null;
        for (let team of teams) {
          if (team.active == true) {
            this.activeTeam = team;
            countActive++;
          }
        }
        if (countActive > 1) {
          this.activeTeam = undefined;
          throw new Error('Error! Multiple active teams for the student, please concat the administrator');
          console.log('Corrupted Team data: ' + JSON.stringify(teams));
        } else if (countActive < 1) {
          throw new Error('No active team for this course. ');
        }
        console.log('ACTIVE', this.activeTeam);
        return this.vlServiceService.getTeamVm(this.activeTeam.id);
      })).subscribe((vmsTeam: Vm[]) => {
        console.log('INSIDE2', vmsTeam);
        this.vms = vmsTeam;
      },
      error => this.alertsService.setAlert('danger', 'Couldn\'t get virtual machines! ' + error)
    );
  }
  changeStatusVm(event: any) { // {id: element.id, status: true}
     this.vlServiceService.changeStatusVm(event.id, event.status).subscribe(value => {
       this.vms.filter(v => v.id == event.id)[0].active = event.status;
       },
       error => this.alertsService.setAlert('danger', 'Couldn\'t change vm status. ' + error));
  }
  deleteVm(vmId: number) {
    this.vlServiceService.deleteVm(vmId).subscribe(value => {
        this.vms = this.vms.filter(v => v.id != vmId);
        this.alertsService.setAlert('info', 'Vm deleted!');
        },
      error => this.alertsService.setAlert('danger', 'Couldn\'t delete vm. ' + error));
  }
}
