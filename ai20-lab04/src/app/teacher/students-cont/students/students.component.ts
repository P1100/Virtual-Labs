import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../../services/student';
import {MatTableDataSource} from '@angular/material/table';
import {MatSidenav} from '@angular/material/sidenav';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';

// TODO: classe molto incasinata, forse si puó migliorare leggibilitá separando e raggruppando funzione per elemento HTML?
@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit, AfterViewInit {
  private _childAllStudents: Student[];
  @Input()
  set childAllStudents(array: Student[]) {
    this._childAllStudents = array;
  }
  get childAllStudents(): Student[] { return this._childAllStudents; }

  private _childEnrolledStudents: Student[];
  @Input()
  set childEnrolledStudents(array: Student[]) {
    this._childEnrolledStudents = array;
  }
  get childEnrolledStudents(): Student[] { return this._childEnrolledStudents; }

  didVote = false;
  @Output()
  addedStudent = new EventEmitter<Student[]>();
  @Output()
  removedStudent = new EventEmitter<Student[]>();
  // vote(agreed: boolean) {
  //   this.voted.emit(agreed);
  //   this.didVote = true;
  // }
  addStudent(event: Event) {
    if (this.studentToAdd && this.childAllStudents.includes(this.studentToAdd) && !this.childEnrolledStudents.includes(this.studentToAdd)) {
      this.childEnrolledStudents.push(this.studentToAdd);
      this.checked.set(this.studentToAdd.id, false);
      this.studentToAdd = null;
      if (this.masterStatus === 1) {
        this.masterStatus = 2;
      }
      this.sortData();
    }
  }
  deleteSelected() {
    this.childEnrolledStudents = this.childEnrolledStudents.filter(x => !this.checked.get(x.id));
    this.dataSource.data = this.childEnrolledStudents;
    this.checked = new Map(this.childEnrolledStudents.map(x => [x.id, false]));
    this.checkedCount = 0;
    this.masterStatus = 0;
  }


  displayedColumnsTable: string[];
  dataSource: MatTableDataSource<Student>;
  checked: Map<number, boolean>;
  checkedCount = 0;
  masterStatus = 0; // {0:unchecked, 1:checked, 2:intermediate};

  filteredOptions: Student[] = this.childAllStudents;
  studentToAdd: Student;

  // MatPaginator Inputs
  length;
  pageSize = 25;
  pageSizeOptions: number[] = [1, 2, 5, 10, 20];

  @ViewChild(MatSidenav) matsidenav: MatSidenav;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor() {
    this.displayedColumnsTable = ['select', 'id', 'firstName', 'lastName', 'group'];
  }
  ngOnInit() {
    this.dataSource = new MatTableDataSource<Student>(this.childEnrolledStudents);
    this.checked = new Map(this.childEnrolledStudents.map(x => [x.id, false]));
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  checkAll(flag) {
    for (const key of this.checked.keys()) {
      this.checked.set(key, flag);
    }
    if (flag) {
      this.checkedCount = this.checked.size;
    } else {
      this.checkedCount = 0;
    }
  }

  toggleCheckboxRow(event: Event, row) {
    console.log('here');
    const checkedSize = this.checked.size;
    if (!this.checked.has(row)) {
      console.log('Error');
      return;
    }
    const currentState = this.checked.get(row);
    if (currentState) {
      this.checkedCount--;
      if (this.checkedCount === 0) {
        this.masterStatus = 0;

      } else if (this.checkedCount === (checkedSize - 1)) {
        this.masterStatus = 2;

      }
    } else {
      this.checkedCount++;
      if (this.checkedCount === checkedSize) {
        this.masterStatus = 1;

      } else if (this.checkedCount === 1) {
        this.masterStatus = 2;

      }
    }
    this.checked.set(row, !currentState);

  }

  toggleCheckboxMaster(event: Event) {
    if (this.masterStatus === 2 || this.masterStatus === 1) {
      this.masterStatus = 0;
      this.checkAll(false);
    } else {
      this.masterStatus = 1;
      this.checkAll(true);
    }
  }

  isChecked(row: string) {
    if (row === 'master') {
      if (this.masterStatus === 0 || this.masterStatus === 2) {
        return false;
      }
      return true;
    } else {
      return this.checked.get(Number(row));
    }
  }

  isIndeterminate() {
    if (this.masterStatus === 2) {
      return true;
    }
    return false;
  }
  displayFn(student: Student): string {
    console.log('s');
    return student && student.firstName && student.lastName && student.id ? student.firstName + ' ' + student.lastName : '';
  }

  activateFilter(event: Event) {
    this.filteredOptions = this.childAllStudents.filter(x => x.firstName.toLowerCase().startsWith((event.target as HTMLInputElement).value.toLowerCase()));
  }

  saveOption(event: MatAutocompleteSelectedEvent) {
    this.studentToAdd = (event).option.value;
  }


  sortChange(sort: MatSort) {
    this.sort = sort;
    this.sortData();
  }
  // TODO: rivedere logica sort bene
  sortData() {
    const data = [...this.childEnrolledStudents]; // .slice() also shallow copy
    if (!this.sort.active || this.sort.direction === '') {
      this.childEnrolledStudents = data;
      this.dataSource.data = this.childEnrolledStudents;
      return;
    }

    this.childEnrolledStudents = data.sort((a, b) => {
      const isAsc = !(this.sort.direction === 'asc'); // the sorting array is more intuitive this way
      switch (this.sort.active) {
        case this.displayedColumnsTable[1]:
          return sortCompare(a[this.displayedColumnsTable[1]], b[this.displayedColumnsTable[1]], isAsc);
        case this.displayedColumnsTable[2]:
          return sortCompare(a[this.displayedColumnsTable[2]], b[this.displayedColumnsTable[2]], isAsc);
        case this.displayedColumnsTable[3]:
          return sortCompare(a[this.displayedColumnsTable[3]], b[this.displayedColumnsTable[3]], isAsc);
        case this.displayedColumnsTable[4]:
          return sortCompare(a[this.displayedColumnsTable[4]], b[this.displayedColumnsTable[4]], isAsc);
        default:
          return 0;
      }
    });
    this.dataSource.data = this.childEnrolledStudents;
  }
}

function sortCompare(a: number | string, b: number | string, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
