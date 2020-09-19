import {Component, Inject, OnDestroy} from '@angular/core';
import {Vm} from '../../models/vm.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {VmEditComponent} from '../vm-edit/vm-edit.component';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {VlServiceService} from '../../services/vl-service.service';
import {Team} from '../../models/team.model';
import {interval, Observable, Subscription} from 'rxjs';
import {startWith, switchMap} from 'rxjs/operators';

export interface vmsValuesInUse {
  vcpuUsed: number,
  ramUsed: number,
  diskUsed: number,
  TotVmUsed: number,
  RunningVmUsed: number
}

@Component({
  selector: 'app-team-edit',
  templateUrl: './team-edit.component.html',
  styles: []
})
export class TeamEditComponent implements OnDestroy {
  realTimeUpdateSub: Subscription;
  vmsStatistics = {} as vmsValuesInUse;
  team: Team;

  constructor(private dialogRef: MatDialogRef<VmEditComponent>, private router: Router, @Inject(MAT_DIALOG_DATA) public data: Team,
              private alertsService: AlertsService, private vlServiceService: VlServiceService) {
    const source: Observable<any> = interval(3000);
    this.team = data;
    this.realTimeUpdateSub = source.pipe(startWith('startImmediately'), switchMap(() => {
      return this.vlServiceService.getTeamVms(this.team.id);
    }))
      .subscribe((vmsTeam: Vm[]) => {
          this.vmsStatistics.vcpuUsed = vmsTeam.reduce((previousValue, currentValue, currentIndex, array) => {
            return currentValue.vcpu + previousValue;
          }, 0);
          this.vmsStatistics.ramUsed = vmsTeam.reduce((previousValue, currentValue, currentIndex, array) => {
            return currentValue.ram + previousValue;
          }, 0);
          this.vmsStatistics.diskUsed = vmsTeam.reduce((previousValue, currentValue, currentIndex, array) => {
            return currentValue.disk + previousValue;
          }, 0);
          this.vmsStatistics.TotVmUsed = vmsTeam.length;
          this.vmsStatistics.RunningVmUsed = vmsTeam.filter(v => v.active).length;
        },
        error => this.alertsService.setAlert('danger', 'Couldn\'t get virtual machines! ' + error)
      );
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    this.vlServiceService.editTeam(this.team).subscribe(
      () => {
        this.dialogRef.close(0);
        this.router.navigateByUrl(this.router.url);
        this.alertsService.setAlert('success', 'Vm edited!');
      },
      e => {
        this.dialogRef.close();
        this.alertsService.setAlert('danger', 'Couldn\'t edit vm. ' + e);
      }
    );
  }
  ngOnDestroy(): void {
    this.realTimeUpdateSub.unsubscribe();
  }
}
