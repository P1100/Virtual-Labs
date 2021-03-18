import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {AfterViewInit, Component, EventEmitter, Input, OnDestroy, Output, ViewChild} from '@angular/core';
import {Team} from '../../../models/team.model';
import {Vm} from '../../../models/vm.model';
import {MatSort} from '@angular/material/sort';
import {AlertsService} from '../../../services/alerts.service';
import {TeamEditComponent} from '../../../dialogs/team-edit/team-edit.component';

@Component({
  selector: 'app-vms-prof',
  templateUrl: './vms-prof.component.html',
  styleUrls: ['./vms-prof.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class VmsProfComponent implements OnDestroy, AfterViewInit {
  displayedColumnsTeams: string[] = ['teamName', 'creator', 'createdDate', 'editResources'];
  columnsToDisplayVm: string[] = ['creator', 'active', 'imageVm'];
  expandedElement: Vm | null;
  dataSourceTeams = new MatTableDataSource<Team>();
  dialogRef: MatDialogRef<any>;
  @Input()
  idStringLoggedStudent;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @Input()
  set teams(array: Team[]) {
    if (array == null) {
      this.dataSourceTeams.data = [];
      return;
    }
    this.dataSourceTeams.data = [...array];
    setTimeout(() => {
      this.dataSourceTeams.sort = this.sort;
    });
  }
  get teams(): Team[] {
    return this.dataSourceTeams.data;
  }
  innerCourseId: string;
  @Input()
  set courseId(id: string) {
    this.innerCourseId = id;
  }
  get courseId() {
    return this.innerCourseId;
  }
  @Output()
  forceRefreshData = new EventEmitter<any>();

  ngAfterViewInit() {
    this.dataSourceTeams.sort = this.sort;
  }

  constructor(private alertsService: AlertsService, public dialog: MatDialog) {
  }

  openEditTeamDialog(team: Team) {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Error: Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(TeamEditComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true, data: team
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
      }, () => this.alertsService.setAlert('danger', 'VM creation dialog error')
    );
  }
  ngOnDestroy(): void {
    this.dialogRef?.close();
  }
}


