import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Team} from '../../../models/team.model';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';
import {Router} from '@angular/router';
import {Vm} from '../../../models/vm.model';
import {VmCreateComponent} from '../../../dialogs/vm-create/vm-create.component';
import {VmEditComponent} from '../../../dialogs/vm-edit/vm-edit.component';
import {vmConstrains} from './vms-stud-cont.component';

export interface vmEditDialogData {
  vm: Vm,
  limits: vmConstrains
}
@Component({
  selector: 'app-vms-stud',
  templateUrl: './vms-stud.component.html',
  styleUrls: ['./vms-stud.component.css']
})
export class VmsStudComponent implements OnDestroy {
  columnsToDisplayVm: string[] = ['creator', 'active', 'imageVm', 'on', 'off', 'delete', 'edit'];
  dataSourceVms = new MatTableDataSource<Vm>();
  dialogRef: MatDialogRef<any>;
  @Input()
  set vms(vms: Vm[]) {
    this.dataSourceVms.data = vms;
  }
  get vms(): Vm[] {
    return this.dataSourceVms.data;
  }
  @Input()
  idStringLoggedStudent;
  @Input()
  activeTeam: Team = null;
  @Output()
  forceRefreshData = new EventEmitter<any>();
  @Output()
  changeStatusVm = new EventEmitter<any>();
  @Output()
  deleteVm = new EventEmitter<any>();
  @Input()
  vmLimits: vmConstrains = null;

  constructor(private alertsService: AlertsService, private courseService: CourseService, public dialog: MatDialog, private router: Router) {
  }

  openCreateVmDialog() {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Error: Dialog stil open while opening a new one');
    }
    if (this.activeTeam?.id == null) {
      this.alertsService.setAlert('danger', 'Error: no active team for this course');
      return;
    }
    const proposalData: Vm = {vcpu: 1, disk:1, ram:1, active:false, studentCreatorId: +this.idStringLoggedStudent, teamId:+this.activeTeam.id};
    this.dialogRef = this.dialog.open(VmCreateComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true, data: proposalData
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
        if (res != undefined) {
          setTimeout(() => {
            this.forceRefreshData.emit(null);
          }, 150);
        }
      }, () => this.alertsService.setAlert('danger', 'VM creation dialog error')
    );
  }
  openEditVmDialog(vm: Vm) {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Error: Dialog stil open while opening a new one');
    }
    if (this.activeTeam?.id == null) {
      this.alertsService.setAlert('danger', 'Error: no active team for this course');
      return;
    }
    let proposalData: vmEditDialogData = {} as vmEditDialogData;
    proposalData.vm = {...vm};
    proposalData.vm.studentCreatorId = +this.idStringLoggedStudent;
    proposalData.vm.teamId = +this.activeTeam.id;
    proposalData.limits = this.vmLimits;
    this.dialogRef = this.dialog.open(VmEditComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true, data: proposalData
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
      this.dialogRef = null;
      if (res != undefined) {
        setTimeout(() => {
          this.forceRefreshData.emit(null);
        }, 150);
      }
      }, () => this.alertsService.setAlert('danger', 'VM edit dialog error')
    );
  }
  ngOnDestroy(): void {
    this.dialogRef?.close();
  }
}


