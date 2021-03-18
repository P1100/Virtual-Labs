import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {Implementation} from '../../models/implementation.model';
import {VlServiceService} from '../../services/vl-service.service';
import {MatTableDataSource} from '@angular/material/table';
import {Image} from '../../models/image.model';

@Component({
  selector: 'app-assignments-history',
  templateUrl: './assignments-history.component.html',
  styleUrls: ['./assignments-history.component.css']
})
export class AssignmentsHistoryComponent {
  dataSource = new MatTableDataSource<Image>();
  displayedColumnsTable = ['createDate', 'link'];
  implementation: Implementation;
  correction: string;
  isDefinitive = false;
  constructor(public dialogRef: MatDialogRef<AssignmentsHistoryComponent>,@Inject(MAT_DIALOG_DATA) public data: Implementation,
              private router: Router, private alertsService: AlertsService, private vlServiceService: VlServiceService) {
    this.implementation = data;
    this.dataSource.data = this.implementation.imageSubmissions;
  }
  submit() {
    let impl: Implementation = {...this.implementation};
    delete impl.imageSubmissions;
    delete impl.student;
    if(this.isDefinitive) {
      impl.status = 'DEFINITIVE';
      impl.definitiveStatus = new Date();
      impl.permanent = true;
      // grade with ngModel
    } else {
      impl.status = 'REVIEWED';
      impl.currentCorrection = this.correction;
    }
    this.vlServiceService.updateImplementation(impl).subscribe(value => {});
    this.dialogRef.close();
  }
}
