import {AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../../models/student.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {AlertsService} from '../../../services/alerts.service';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {CourseService} from '../../../services/course.service';
import {SelectionModel} from '@angular/cdk/collections';
import {TeamProposeComponent} from '../../../dialogs/team-propose/team-propose.component';
import {Team} from '../../../models/team.model';
import {MatPaginator} from '@angular/material/paginator';

export interface dialogProposalData {
  courseId: string,
  membersWithProposerFirst: Student[]
}

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class TeamsComponent implements AfterViewInit, OnDestroy {
  displayedColumnsTable1: string[] = ['select', 'id', 'firstName', 'lastName', 'email'];
  columnsToDisplayProposals: string[] =   ['nav', 'proposer', 'name']; //, 'accept', 'reject'
  columnsToDisplayTeam: string[] =   ['name', 'active', 'disabled'];
  columnsToDisplayStudent: string[] = ['id', 'firstName', 'lastName'];
  expandedElement: Student | null;

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  dataSourceEnrolledNoTeams = new MatTableDataSource<Student>();
  dataSourceTeams = new MatTableDataSource<Team>();
  dialogRef: MatDialogRef<any>;
  selection = new SelectionModel<Student>(true, []);
  @Input()
  activeTeam: Team = null;
  @Input()
  set notActiveTeams(t: Team[]) {
    this.dataSourceTeams.data = t;
  }
  get notActiveTeams(): Team[] {
    return this.dataSourceTeams.data;
  }
  innerCourseId: string;
  @Input()
  set courseId(id: string) {
    this.innerCourseId = id;
    this.selection.clear();
  }
  get courseId() {
    return this.innerCourseId;
  }
  @Input()
  courseMin: number;
  @Input()
  courseMax: number;
  @Output()
  forceUploadData = new EventEmitter<any>();
  loggedUserStudent: Student;
  @Input()
  set enrolledWithoutTeams(array: Student[]) {
    console.log('init', array.length, array, localStorage.getItem('id'));
    this.loggedUserStudent = array.find(s => s.id == +localStorage.getItem('id'));
    console.log('logged user', this.loggedUserStudent, array.indexOf(this.loggedUserStudent));
    array.splice(array.indexOf(this.loggedUserStudent), 1);
    console.log('spliced', array.length, array,);
    this.dataSourceEnrolledNoTeams.data = [...array];
    // Should help making sure table data is loaded when sort is assigned
    setTimeout(() => {
      this.dataSourceEnrolledNoTeams.sort = this.sort;
    })
  }
  get enrolledWithoutTeams(): Student[] {
    return this.dataSourceEnrolledNoTeams.data;
  }
  @Input()
  hideAllGUItillActiveTeamIsChecked: boolean;

  constructor(private alertsService: AlertsService, private courseService: CourseService, public dialog: MatDialog,) {
  }
  ngAfterViewInit() {
    this.dataSourceEnrolledNoTeams.paginator = this.paginator;
    this.dataSourceEnrolledNoTeams.sort = this.sort;
  }

  openProposeTeamDialog() {
    console.log('DIALOG', this.courseMin, this.courseMax);
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Error: Dialog stil open while opening a new one');
    }
    if (this.selection.selected.length == 0) {
      this.alertsService.setAlert('warning', 'No students selected!');
      return;
    } /* -1 because logged in user is automatically added */
    if (this.selection.selected.length < (this.courseMin-1)) {
      this.alertsService.setAlert('warning', `Selected less than ${this.courseMin-1} students (course minimum)`);
      return;
    }
    if (this.selection.selected.length > (this.courseMax-1)) {
      this.alertsService.setAlert('warning', `Selected more than ${this.courseMax-1} students, (course maximum)`);
      return;
    }
    const proposalData: dialogProposalData = {courseId: this.courseId, membersWithProposerFirst: [this.loggedUserStudent, ...this.selection.selected]};
    this.dialogRef = this.dialog.open(TeamProposeComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true, data: proposalData
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
        if (res != undefined) {
          this.forceUploadData.emit(null);
        }
      }, () => this.alertsService.setAlert('danger', 'Team Proposal dialog error')
    );
  }
  /** Whether the number of selected elements matches the total number of rows. */
  checkboxIsAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSourceEnrolledNoTeams.data.length;
    return numSelected === numRows;
  }
  // /** Selects all rows if they are not all selected; otherwise clear selection. */
  // checkboxMasterToggle() {
  //   this.checkboxIsAllSelected() ?
  //     this.selection.clear() :
  //     this.dataSourceEnrolledNoTeams.data.forEach(row => this.selection.select(row));
  // }
  idOpenExpansionStatus: boolean[];
  checkboxChangeSelection(row: Student) {
    this.selection.toggle(row);
  }
  ngOnDestroy(): void {
    this.dialogRef?.close();
  }
  checkboxDisableMinMax(row: Student) {
    if (!this.selection.isSelected(row) && this.selection.selected.length >= (this.courseMax - 1)) {
      return true;
    }
    return false;
  }
  removeDisabledTeams() {
  }
}
