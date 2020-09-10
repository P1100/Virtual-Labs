import {Component, Input, ViewChild} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {AlertsService} from '../../../services/alerts.service';
import {TeamProposeComponent} from '../../../dialogs/team-propose/team-propose.component';
import {Assignment} from '../../../models/assignment.model';
import {Implementation} from '../../../models/implementation.model';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {AssignmentsHistoryComponent} from '../../../dialogs/assignments-history/assignments-history.component';

@Component({
  selector: 'app-assignment-prof',
  templateUrl: './assignment-prof.component.html',
  styleUrls: ['./assignment-prof.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AssignmentProfComponent {
  columnsToDisplay: string[] = ['nav', 'name', 'releaseDate'];
  columnsToDisplayImplementation: string[] = ['firstName', 'lastName', 'id', 'status', 'timestamp'];
  expandedElement: Implementation | null;
  // statusArray = ['NULL', 'READ', 'SUBMITTED', 'REVIEWED', 'DEFINITIVE'];
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  dialogRef: MatDialogRef<any>;
  dataSourceAssignments = new MatTableDataSource<Assignment>();
  filterNull = false;
  @Input()
  set assignments(array: Assignment[]) {
    if (array == null) {
      this.dataSourceAssignments.data = [];
      return;
    }
    this.dataSourceAssignments.data = [...array];
    // Should help making sure table data is loaded when sort is assigned
    setTimeout(() => {
      this.dataSourceAssignments.sort = this.sort;
    });
  }
  get assignments(): Assignment[] {
    return this.dataSourceAssignments.data;
  }
  constructor(private alertsService: AlertsService, public dialog: MatDialog) {
  }
  openShowHistory(impl: Implementation) {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Error: Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(AssignmentsHistoryComponent, {
      maxWidth: '800px', autoFocus: false, hasBackdrop: true, disableClose: false, closeOnNavigation: true, data: impl
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
      }, () => this.alertsService.setAlert('danger', 'Implementations history dialog error')
    );
  }
  logTest(Status: any) {
    console.log(Status);
  }
}
