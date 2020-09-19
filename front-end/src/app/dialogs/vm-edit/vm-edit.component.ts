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
  startingVcpuVm: number;
  startingRamVm: number;
  startingDiskVm: number;

  constructor(private dialogRef: MatDialogRef<VmEditComponent>, private router: Router, @Inject(MAT_DIALOG_DATA) public data: vmEditDialogData,
              private alertsService: AlertsService, private vlServiceService: VlServiceService) {
    this.vm = {...data.vm};
    this.limits = {...data.limits};
    this.startingVcpuVm = this.vm.vcpu;
    this.startingRamVm = this.vm.ram;
    this.startingDiskVm = this.vm.disk;
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    this.vlServiceService.editVm(this.vm).subscribe(
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
}
