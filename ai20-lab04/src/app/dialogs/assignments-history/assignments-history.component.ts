import {Component, Inject, OnInit} from '@angular/core';
import {CourseService} from '../../services/course.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogCourseData} from '../../r0-topheader-leftsidebar/home.component';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {Course} from '../../models/course.model';
import {Implementation} from '../../models/implementation.model';

@Component({
  selector: 'app-assignments-history',
  templateUrl: './assignments-history.component.html',
  styleUrls: ['./assignments-history.component.css']
})
export class AssignmentsHistoryComponent {
  displayedColumnsTable = ['createDate', 'link'];
  private implementation: Implementation;
  value: any;
  isDefinitive = false;
  grade = null;
  constructor(private dialogRef: MatDialogRef<AssignmentsHistoryComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Implementation, private router: Router, private alertsService: AlertsService) {
    this.implementation = data;
    console.log(data);
  }
  submit() {}
}
