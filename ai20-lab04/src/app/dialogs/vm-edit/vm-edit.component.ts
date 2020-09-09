import {Component, Inject} from '@angular/core';
import {Vm} from '../../models/vm.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {VlServiceService} from '../../services/vl-service.service';
import {vmEditDialogData} from '../../r2-inner-tab/student/vms/vms-stud.component';
import {vmConstrains} from '../../r2-inner-tab/student/vms/vms-stud-cont.component';

@Component({
  selector: 'app-vm-edit',
  templateUrl: './vm-edit.component.html',
  styles: []
})
export class VmEditComponent {
  vm: Vm = {vcpu: null, disk: null, ram: null, active: false};
  limits: vmConstrains;

  constructor(private dialogRef: MatDialogRef<VmEditComponent>, private router: Router, @Inject(MAT_DIALOG_DATA) public data: vmEditDialogData,
              private alertsService: AlertsService, private vlServiceService: VlServiceService) {
    this.vm = {...data.vm};
    this.limits = {...data.limits};
    console.log('EDIT DIALOG:', this.data, this.vm, this.limits);
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    console.log('submit');
    this.vlServiceService.createVm(this.vm).subscribe(
      () => {
        this.dialogRef.close(0);
        this.router.navigateByUrl(this.router.url);
        this.alertsService.setAlert('success', 'Vm created!');
      },
      e => {
        this.dialogRef.close();
        this.alertsService.setAlert('danger', 'Couldn\'t create vm. ' + e);
      }
    );
  }
}
