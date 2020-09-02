import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Student} from '../../../models/student.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {animate, state, style, transition, trigger} from '@angular/animations';

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
export class TeamsComponent implements OnInit {
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @Input()
  set enrolledWithoutTeams(array: Student[]) {
    this.dataSource.data = [...array];
    // this.checked = new Map(array.map(x => [+x.id, false]));
  }
  get enrolledWithoutTeams(): Student[] {
    return this.dataSource.data;
  }
  displayedColumns: string[] = ['select', 'id', 'firstName', 'lastName', 'email'];

  constructor() {
  }

  ngOnInit() {
    this.dataSource.sort = this.sort;
  }

  openProposeTeamDialog() {
  }
}
