import {Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../../models/student.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {CourseAddComponent} from '../../../dialogs/course-add/course-add.component';
import {AlertsService} from '../../../services/alerts.service';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {CourseService} from '../../../services/course.service';

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
export class TeamsComponent implements OnInit, OnDestroy {
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  displayedColumns: string[] = ['select', 'id', 'firstName', 'lastName', 'email'];
  private enrolledSelectedForProposal: Student[];
  dialogRef: MatDialogRef<any>;
  /* Checkbox Logic */
  checkboxMasterCompleted = false;
  checkboxMasterIndeterminate = false;
  checked: Map<number, boolean> = new Map();

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  @Output()
  forceUploadData = new EventEmitter<any>();

  @Input()
  set enrolledWithoutTeams(array: Student[]) {
    this.dataSource.data = [...array];
    this.checked = new Map(array.map(x => [+x.id, false]));
  }
  get enrolledWithoutTeams(): Student[] {
    return this.dataSource.data;
  }

  constructor(private alertsService: AlertsService, private courseService: CourseService, public dialog: MatDialog,) {
  }
  ngOnInit() {
    this.dataSource.sort = this.sort;
  }

  openProposeTeamDialog() {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Dialog stil open while opening a new one');
    }
    let enrolledSelectedForProposal;
    this.dialogRef = this.dialog.open(CourseAddComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true,
      data: {studentsProposal: enrolledSelectedForProposal}
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
      this.dialogRef = null;
      if (res != undefined) {
        this.forceUploadData.emit(null);
      }
      }, () => this.alertsService.setAlert('danger', 'Team Proposal dialog error')
    );
  }
  // change selection, sort update, paginator update
  private updateMasterCheckbox() {
    const entriesCheckbox = [...this.checked.entries()];
    this.checkboxMasterCompleted = entriesCheckbox.every(t => t[1] === true);
    this.checkboxMasterIndeterminate = !this.checkboxMasterCompleted && entriesCheckbox.filter(x => x[1] === true).length > 0;
  }
  checkboxChangeSelection({checked}: { checked: boolean }, id) { // destructuring
    console.log(this.checked);
    // console.log('sads',[...this.checked.values()],[...this.checked.values()].filter((x: boolean) => x),
    //   [...this.checked.values()].filter((x: boolean) => x).length);
    // if ([...this.checked.values()].filter((x: boolean) => x).length >= 4)
    //   return;
    // console.log('set', id, checked);
    // this.checked.set(+id, checked);
    // this.updateMasterCheckbox();
  }
  checkboxIsChecked(id: number) {
    // console.log(this.checked, this.checked.get(+id));
    // console.log('isChecked', id, this.checked.get(+id));
    return this.checked.get(+id);
  }
  checkboxSetAll(completed: boolean) {
    [...this.checked.entries()].forEach((t: [number, boolean]) => this.checked.set(+t[0], completed)); // converting iterable to array (spread)
    this.checkboxMasterCompleted = completed;
    this.checkboxMasterIndeterminate = false;
  }
  ngOnDestroy(): void {
    this.dialogRef.close();
  }
}
