import {Component, Inject} from '@angular/core';
import {AppSettings} from '../../app-settings';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {Vm} from '../../models/vm.model';
import {VlServiceService} from '../../services/vl-service.service';

@Component({
  selector: 'app-vm-create',
  templateUrl: './vm-create.component.html',
  styles: []
})
export class VmCreateComponent {
  vm: Vm = new Vm(null, null, null);
  checkboxNoValidate = false;
  showCheckboxNoValidateForTesting = AppSettings.devtest;

  constructor(private dialogRef: MatDialogRef<VmCreateComponent>, private router: Router, @Inject(MAT_DIALOG_DATA) public data: Vm,
              private alertsService: AlertsService, private vlServiceService: VlServiceService) {
    this.vm = {...data};
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
