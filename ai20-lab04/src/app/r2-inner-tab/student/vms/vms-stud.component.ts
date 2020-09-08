import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Team} from '../../../models/team.model';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';
import {Router} from '@angular/router';
import {Vm} from '../../../models/vm.model';
import {VmCreateComponent} from '../../../dialogs/vm-create/vm-create.component';

@Component({
  selector: 'app-vms-stud',
  templateUrl: './vms-stud.component.html',
  styleUrls: ['./vms-stud.component.css']
})
export class VmsStudComponent implements OnDestroy {
  columnsToDisplayVm: string[] = ['studentCreatorId', 'status', 'link', 'on', 'off', 'delete', 'edit'];
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
  forceUploadData = new EventEmitter<any>();

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
    const proposalData: Vm = new Vm(0, 0, 0, false, +this.idStringLoggedStudent, +this.activeTeam.id);
    this.dialogRef = this.dialog.open(VmCreateComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true, data: proposalData
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
        if (res != undefined) {
          setTimeout(() => {
            this.forceUploadData.emit(null);
          }, 150);
        }
      }, () => this.alertsService.setAlert('danger', 'VM creation dialog error')
    );
  }
  ngOnDestroy(): void {
    this.dialogRef?.close();
  }
}


