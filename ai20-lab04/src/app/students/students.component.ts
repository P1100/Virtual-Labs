import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {Student} from '../student.module';
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

  columnsToDisplay = ['select', 'id', 'name', 'firstName'];
  checked;
  dataSource: MatTableDataSource<Student>;
  checked_count = 0;
  master_status = 0; //0:unchecked, 1:checked, 2:intermediate;

  filteredOptions: Student[] = this.allStudents;
  studentToAdd: Student;

  @ViewChild(MatSidenav) matsidenav: MatSidenav;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  value = 'clear me';

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
      this.checked_count = this.checked.size;
    } else {
      this.checked_count = 0;
    }
  }

  toggleCheckboxRow(event: Event, row) {
    console.log('here');
    let checked_size = this.checked.size;
    if (!this.checked.has(row)) {
      console.log('Error');
      return;
    }
    let current_state = this.checked.get(row);
    if (current_state) {
      this.checked_count--;
      if (this.checked_count === 0) {
        this.master_status = 0;

      } else if (this.checked_count === (checked_size - 1)) {
        this.master_status = 2;

      }
    } else {
      this.checked_count++;
      if (this.checked_count === checked_size) {
        this.master_status = 1;

      } else if (this.checked_count == 1) {
        this.master_status = 2;

      }
    }
    this.checked.set(row, !current_state);

  }

  toggleCheckboxMaster(event: Event) {
    if (this.master_status === 2 || this.master_status === 1) {
      console.log('check master --> uncheked');
      this.master_status = 0;
      this.checkAll(false);
    } else {
      console.log('check master --> cheked');
      this.master_status = 1;
      this.checkAll(true);
    }
  }

  isChecked(row) {
    if (row === 'master') {
      if (this.master_status === 0 || this.master_status === 2) {
        return false;
      }
      console.log('checked true');
      return true;
    } else {
      return this.checked.get(row);
    }

  }

  isIndeterminate() {
    if (this.master_status === 2) {
      console.log('indeterminate true');
      return true;
    }
    return false;
  }

  deleteSelected() {
    this.enrolledStudents = this.enrolledStudents.filter(x => !this.checked.get(x.id));
    this.dataSource.data = this.enrolledStudents;
    this.checked = new Map(this.enrolledStudents.map(x => [x.id, false]));
    this.checked_count = 0;
    this.master_status = 0;
  }

  displayFn(student: Student): string {
    console.log('s');
    return student && student.name && student.firstName && student.id ? student.name + ' ' + student.firstName : '';
  }

  activateFilter(event: Event) {
    this.filteredOptions = this.allStudents.filter(x => x.name.toLowerCase().startsWith((<HTMLInputElement> event.target).value.toLowerCase()));
  }

  saveOption(event: MatAutocompleteSelectedEvent) {
    this.studentToAdd = (event as unknown as MatAutocompleteSelectedEvent).option.value;
  }

  addStudent(event: Event) {
    if (this.studentToAdd && this.allStudents.includes(this.studentToAdd) && !this.enrolledStudents.includes(this.studentToAdd)) {
      this.enrolledStudents.push(this.studentToAdd);
      this.checked.set(this.studentToAdd.id, false);
      this.studentToAdd = null;
      if (this.master_status === 1) {
        this.master_status = 2;
      }
      this.sortData();
    }
  }

  changeSort(sort: MatSort) {
    this.sort = sort;
    this.sortData();
  }

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
