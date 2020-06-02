import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {Student} from '../../../student';
import {MatTableDataSource} from '@angular/material/table';
import {MatSidenav} from '@angular/material/sidenav';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit, AfterViewInit {
  @Input() enrolledStudents: Student[];
  @Input() allStudents: Student[];

  displayedColumns: string[] = ['select', 'id', 'name', 'firstName', 'group'];
  checked;
  dataSource: MatTableDataSource<Student>;
  checkedCount = 0;
  masterStatus = 0; // {0:unchecked, 1:checked, 2:intermediate};

  filteredOptions: Student[] = this.allStudents;
  studentToAdd: Student;

  // MatPaginator Inputs
  length;
  pageSize = 12;
  pageSizeOptions: number[] = [1, 2, 5, 10, 20];

  @ViewChild(MatSidenav) matsidenav: MatSidenav;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource = new MatTableDataSource<Student>(this.enrolledStudents);
    this.checked = new Map(this.enrolledStudents.map(x => [x.id, false]));
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  checkAll(flag) {
    for (let key of this.checked.keys()) {
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
      return this.checked.get(row);
    }
  }

  isIndeterminate() {
    if (this.masterStatus === 2) {
      return true;
    }
    return false;
  }

  deleteSelected() {
    this.enrolledStudents = this.enrolledStudents.filter(x => !this.checked.get(x.id));
    this.dataSource.data = this.enrolledStudents;
    this.checked = new Map(this.enrolledStudents.map(x => [x.id, false]));
    this.checkedCount = 0;
    this.masterStatus = 0;
  }

  displayFn(student: Student): string {
    console.log('s');
    return student && student.name && student.firstName && student.id ? student.name + ' ' + student.firstName : '';
  }

  activateFilter(event: Event) {
    this.filteredOptions = this.allStudents.filter(x => x.name.toLowerCase().startsWith((<HTMLInputElement> event.target).value.toLowerCase()));
  }

  saveOption(event: MatAutocompleteSelectedEvent) {
    this.studentToAdd = (event).option.value;
  }

  addStudent(event: Event) {
    if (this.studentToAdd && this.allStudents.includes(this.studentToAdd) && !this.enrolledStudents.includes(this.studentToAdd)) {
      this.enrolledStudents.push(this.studentToAdd);
      this.checked.set(this.studentToAdd.id, false);
      this.studentToAdd = null;
      if (this.masterStatus === 1) {
        this.masterStatus = 2;
      }
      this.sortData();
    }
  }

  changeSort(sort: MatSort) {
    this.sort = sort;
    this.sortData();
  }

  // TODO: rivedere logica sort bene
  sortData() {
    const data = this.enrolledStudents.slice();
    if (!this.sort.active || this.sort.direction === '') {
      this.enrolledStudents = data;
      this.dataSource.data = this.enrolledStudents;
      return;
    }

    this.enrolledStudents = data.sort((a, b) => {
      const isAsc = this.sort.direction === 'asc';
      switch (this.sort.active) {
        case 'name':
          return compare(a.name, b.name, isAsc);
        case 'id':
          return compare(a.id, b.id, isAsc);
        case 'firstName':
          return compare(a.firstName, b.firstName, isAsc);
        default:
          return 0;
      }
    });
    this.dataSource.data = this.enrolledStudents;
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
