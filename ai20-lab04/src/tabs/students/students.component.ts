import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../app/model/student.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {filter, map, startWith} from 'rxjs/operators';

// TODO: classe molto incasinata, forse si puó migliorare leggibilitá separando e raggruppando funzioni per elemento HTML?
@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
// Non vengono usati Observables in questa classe, perché tutta la comunicazione asincrona con server/servizio avviene nel componente container
export class StudentsComponent implements OnInit, AfterViewInit {
  @Output('enrolledEvent')
  enrolledEvent = new EventEmitter<Student[]>();
  @Output('disenrolledEvent')
  disenrolledEvent = new EventEmitter<Student[]>();
  // Needed to get paginator instance
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // Needed sort variable
  @ViewChild(MatSort) sort: MatSort;
  // displayedColumnsTable is based on Student class (manual synch), controls various formatting elements
  displayedColumnsTable: string[] = ['select', 'id', 'firstName', 'lastName', 'group'];
  // MatPaginator Inputs
  length: number; // The current total number of items being paged. Read only
  pageSize = 25;
  pageSizeOptions: number[] = [1, 2, 5, 10, 20];
  myControl = new FormControl();
  // Table data, collegata direttamente enrolled tramite setter/getter (binding diretto di enrolledStudents dal padre)
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  selectedStudentToAdd: Student = null;
  // Checkbox
  checked: Map<number, boolean> = null;
  checkedCount = 0;
  masterStatus = 0; // {0:unchecked, 1:checked, 2:intermediate};
  // Used in AutoComplete. It's the list of all students but at times filtered (so cant be merged in only one var)
  filteredOptions$: Observable<Student[]>;
  // Private variable used to get data from the service (superset of filteredOptions, used in autocomplete)
  @Input()
  private students: Student[];
  // filteredOptions: Student[] = [];
  constructor() {
    // console.log('# students.constuctor selectedStudentToAdd:\n' + this.selectedStudentToAdd);
  }
  get enrolled(): Student[] {
    return this.dataSource.data;
  }
  @Input()
  set enrolled(array: Student[]) {
    this.dataSource.data = [...array];
    this.sortData();
    this.checked = new Map(array.map(x => [x.id, false]));
    this.checkedCount = 0;
    this.masterStatus = 0;
  }
  ngOnInit() {
    // arrays students and enrolled are automatically passed by the parent component, studentsContainer
    // this.filteredOptions = this.students;
    this.filteredOptions$ = this.myControl.valueChanges
      .pipe(
        startWith(''),
        // When option is selected, value becomes a Student object... not a string. Code below commented (console.logs) was to test that
        filter(value => ((typeof value) === 'string')),
        map((value: string): Student[] => {
          // console.log('@ value isNull ' + (value === null));
          // console.log('@ value isUndefined ' + (value === undefined));
          // console.log('@ value is type string ' + ((typeof value) === 'string'));
          // console.log('@ value is type object ' + ((typeof value) === 'object'));
          // console.log('StudentsComponent.ngOnInit filteredOptions$ value:\ntypeof->' + typeof value + '(\"' + value + '\")');
          // console.log('@ filteredOptions$' + JSON.stringify(this.filteredOptions$)); --> no, é un observable, non viene stampato
          return this.students.filter(x => x.firstName.toLowerCase().startsWith(value.trim().toLowerCase()));
        }),
      );
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  // Data and sort update is done by the angular property binding, automatically (@Input() set enrolled)
  studentsAdd() {
    if (this.selectedStudentToAdd && this.students.includes(this.selectedStudentToAdd)
      && !this.enrolled.includes(this.selectedStudentToAdd)) {
      this.enrolledEvent.emit([this.selectedStudentToAdd]);
      this.checked.set(this.selectedStudentToAdd.id, false);
      this.selectedStudentToAdd = null;
      if (this.masterStatus === 1) {
        this.masterStatus = 2;
      }
    }
  }
  // Passing the students to remove (whole Students array, not just the id)
  studentsRemove() {
    const selectedStudentToRemove = this.enrolled.filter(x => this.checked.get(x.id));
    this.disenrolledEvent.emit(selectedStudentToRemove);
    this.checked = new Map(this.enrolled.map(x => [x.id, false]));
    this.checkedCount = 0;
    this.masterStatus = 0;
  }
  sortChange(sort: MatSort) {
    console.log('# students.sortChange selectedStudentToAdd: ' + JSON.stringify(this.selectedStudentToAdd));
    this.sort = sort;
    this.sortData();
  }
  // changes dataSource.data, which is linked by setter to the template component. As soon as data gets updated, sort is called again (look enrolled setter)
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

  checkboxCheckAll(flag) {
    console.log('checkboxCheckAll');
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
    const checkedSize = this.checked.size;
    if (!this.checked.has(row)) {
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
  // Nota: all'intenrno il this non é il componente student, ma bensí il componente MatAutocomplete (che chiama la funzione)
  autocompleteDisplayFunction(student: Student): string {
    return student && student.firstName && student.lastName && student.id ? student.firstName + ' ' + student.lastName : '';
  }
  autocompleteSaveOption(event: MatAutocompleteSelectedEvent) {
    this.selectedStudentToAdd = (event).option.value;
    console.log('# StudentsComponent.autocompleSave added student' + JSON.stringify(this.selectedStudentToAdd));
  }
// TODO: delete later, if things works without issues
  // @ViewChild(MatSidenav) matsidenav: MatSidenav;

  /*
  // NOT USED ANYMORE, old implementation with the following in tag  <mat-form-field><input
  //   <!--             (keyup)="autocompleteActivateFilter($event)"-->
  autocompleteActivateFilter(event: Event) {
    // @ts-ignore
    console.log(event.target.value);
    console.log(this.filteredOptions === this.students);
    this.filteredOptions = this.students.filter(x => x.firstName.toLowerCase().startsWith((event.target as HTMLInputElement).value.toLowerCase()));
    console.log(this.filteredOptions === this.students);
  }
*/
  // Private variable used to get data from the service (superset of filteredOptions, used in autocomplete)
  // get students(): Student[] {
  //   return this._childAllStudents;
  // }
  // @Input()
  // set students(array: Student[]) {
  //   this._childAllStudents = [...array];
  // }
  // Using directly dataSource.data instead of a private variable (_childEnrolledStudents), because data is only used/bound to the table
}

function sortCompare(a: number | string, b: number | string, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
