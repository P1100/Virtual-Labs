import {Component, Inject, OnInit} from '@angular/core';
import {CourseService} from '../../services/course.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogCourseData} from '../../r0-topheader-leftsidebar/home.component';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {Course} from '../../models/course.model';
import {Implementation} from '../../models/implementation.model';
import {VlServiceService} from '../../services/vl-service.service';
import {MatTableDataSource} from '@angular/material/table';
import {Team} from '../../models/team.model';
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
      impl.definitiveStatus = new Date();
      impl.permanent = true;
      // grade with ngModel
    } else {
      impl.currentCorrection = this.correction;
    }
    this.vlServiceService.updateImplementation(impl).subscribe(value => {});
    this.dialogRef.close();
  }
}
