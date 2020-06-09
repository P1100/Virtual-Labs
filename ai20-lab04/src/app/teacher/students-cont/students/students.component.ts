import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../../services/student';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {FormControl} from '@angular/forms';

// TODO: classe molto incasinata, forse si puó migliorare leggibilitá separando e raggruppando funzioni per elemento HTML?
@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit, AfterViewInit {
  myControl = new FormControl();
  // displayedColumnsTable is based on Student class (manual synch), controls various formatting elements
  displayedColumnsTable: string[];
  // Table data
  dataSource: MatTableDataSource<Student>;
  selectedStudentToAdd: Student;
  // Checkbox
  checked: Map<number, boolean>;
  checkedCount = 0;
  masterStatus = 0; // {0:unchecked, 1:checked, 2:intermediate};
  // AutoComplete
  filteredOptions: Student[];
  // MatPaginator Inputs
  length: number;
  pageSize: number;
  pageSizeOptions: number[];

  // used in input getter/setter
  private _childAllStudents: Student[];

  // Needed to get paginator instance
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // Needed sort variable
  @ViewChild(MatSort) sort: MatSort;

  @Output()
  addedStudents = new EventEmitter<Student[]>();
  @Output()
  removedStudents = new EventEmitter<Student[]>();

  constructor() {
    this.displayedColumnsTable = ['select', 'id', 'firstName', 'lastName', 'group'];
    this.pageSizeOptions = [1, 2, 5, 10, 20];
    this.pageSize = 25;
    this.dataSource = new MatTableDataSource<Student>();
  }
  // Private variable used to get data from the service (superset of filteredOptions, used in autocomplete)
  get childAllStudents(): Student[] {
    return this._childAllStudents;
  }
  @Input()
  set childAllStudents(array: Student[]) {
    this._childAllStudents = [...array];
  }
  // Using directly dataSource.data instead of a private variable (_childEnrolledStudents), because data is only used/bound to the table
  get childEnrolledStudents(): Student[] {
    return this.dataSource.data;
  }
  @Input()
  set childEnrolledStudents(array: Student[]) {
    this.dataSource.data = [...array];
    this.sortData();
  }
  // Data and sort update is done by the angular property binding, automatically (@Input() set childEnrolledStudents)
  studentsAdd() {
    if (this.selectedStudentToAdd && this.childAllStudents.includes(this.selectedStudentToAdd)
      && !this.childEnrolledStudents.includes(this.selectedStudentToAdd)) {
      this.addedStudents.emit([this.selectedStudentToAdd]);
      this.checked.set(this.selectedStudentToAdd.id, false);
      this.selectedStudentToAdd = null;
      if (this.masterStatus === 1) {
        this.masterStatus = 2;
      }
    }
  }
  // Passing the students to remove (whole Student, not just the id)
  studentsRemove() {
    const selectedStudentToRemove = this.childEnrolledStudents.filter(x => this.checked.get(x.id));
    this.removedStudents.emit(selectedStudentToRemove);
    this.checked = new Map(this.childEnrolledStudents.map(x => [x.id, false]));
    this.checkedCount = 0;
    this.masterStatus = 0;
  }
  sortChange(sort: MatSort) {
    this.sort = sort;
    this.sortData();
  }
  sortData() {
    if (!this.sort || !this.sort.active || this.sort.direction === '') {
      return;
    }
    this.dataSource.data = [...this.dataSource.data].sort((a, b) => {
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
  }
  ngOnInit() {
    this.checked = new Map(this.childEnrolledStudents.map(x => [x.id, false]));
    this.filteredOptions = this.childAllStudents;
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  checkboxCheckAll(flag) {
    for (const key of this.checked.keys()) {
      this.checked.set(key, flag);
    }
    if (flag) {
      this.checkedCount = this.checked.size;
    } else {
      this.checkedCount = 0;
    }
  }

  checkboxToggleRow(event: Event, row) {
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
  checkboxToggleMaster() {
    if (this.masterStatus === 2 || this.masterStatus === 1) {
      this.masterStatus = 0;
      this.checkboxCheckAll(false);
    } else {
      this.masterStatus = 1;
      this.checkboxCheckAll(true);
    }
  }
  checkboxIsChecked(row: string) {
    if (row === 'master') {
      return !(this.masterStatus === 0 || this.masterStatus === 2);

    } else {
      return this.checked.get(Number(row));
    }
  }
  checkboxIsIndeterminate() {
    return this.masterStatus === 2;

  }
  autocompleteDisplayFunction(student: Student): string {
    return student && student.firstName && student.lastName && student.id ? student.firstName + ' ' + student.lastName : '';
  }

  autocompleteActivateFilter(event: Event) {
    this.filteredOptions = this.childAllStudents.filter(x => x.firstName.toLowerCase().startsWith((event.target as HTMLInputElement).value.toLowerCase()));
  }

  autocompleteSaveOption(event: MatAutocompleteSelectedEvent) {
    this.selectedStudentToAdd = (event).option.value;
  }
// TODO: delete later, if things works without issues
  // @ViewChild(MatSidenav) matsidenav: MatSidenav;
}

// ################################### END OF COMPONENT #########################################

function sortCompare(a: number | string, b: number | string, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
