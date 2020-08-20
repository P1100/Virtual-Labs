import {AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../models/student.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort, Sort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {FormControl} from '@angular/forms';
import {Observable, Subscription} from 'rxjs';
import {filter, map, startWith} from 'rxjs/operators';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
/* Data is always obtained from the container component, which calls the service, which calls the back end */
export class StudentsComponent implements OnInit, AfterViewInit, OnDestroy {
  @Input()
  // Superset of filteredOptions, used in autocomplete
  private students: Student[];
  // displayedColumnsTable is based on Student model (manual sync). It controls various formatting elements
  displayedColumnsTable: string[] = ['select', 'id', 'firstName', 'lastName', 'team']; // email removed
  // enrolled is saved with a get/set syntax
  get enrolled(): Student[] {
    return this.dataSource.data;
  }
  // Update enrolled students data from, when new one is sent by the container comp. Reset all
  @Input()
  set enrolled(array: Student[]) {
    this.dataSource.data = [...array];
    this.sortData();
    this.checked = new Map(array.map(x => [+x.id, false]));
    this.checkboxMasterCompleted = false;
    this.checkboxMasterIndeterminate = false;
    this.showCheckboxDeselectAllToolbar = false;
    this.showCheckboxDeselectAllToolbar = false;
  }
  autocompleteControl = new FormControl();
  // Used in AutoComplete. It's the list of all students but at times filtered (so cant be merged in only one var)
  filteredOptions$: Observable<Student[]> = null;
  // NEEDED logic to ensure student data is loaded at first click on the autocomplete // TODO: review later, there are better ways besides valueChanges
  @Input()
  set setAutocompleteInit(flag: string) {
    if (flag != null) {
      this.filteredOptions$ = this.autocompleteControl.valueChanges
        .pipe(
          startWith(''),
          // // WARNING: When option is selected, value becomes a Student object... not a string. Code below commented (console.logs) was to test that
          filter(value => ((typeof value) === 'string')),
          // tap(() => console.log('filteredOptions$', this.students, this.enrolled)),
          map((value: string): Student[] => {
            return this.students.filter(s => {
              let notcontains = true;
              for (const student of this.enrolled) {
                // tslint:disable-next-line:triple-equals
                if (student.id == s.id) {
                  notcontains = false;
                }
              }
              return notcontains;
            })
              ?.filter(x => (x.firstName + ' ' + x.lastName + ' ' + x.firstName + x.id).toLowerCase().includes(value.trim().toLowerCase()))
              ?.sort((a, b) => {
                return sortCompare(a.lastName, b.lastName, true);
              });
          })
        );
    }
  }
  // Table data, connected directly to the data sent from the cont component (see above)
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  @Output()
  enrolledEvent = new EventEmitter<Student[]>();
  @Output()
  disenrolledEvent = new EventEmitter<Student[]>();
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // MatPaginator Inputs
  length: number; // The current total number of items being paged. Read only
  pageSize = 25;
  pageSizeOptions: number[] = [1, 2, 5, 10, 20];
  /* Checkbox Logic */
  checkboxMasterCompleted = false;
  checkboxMasterIndeterminate = false;
  showCheckboxSelectAllToolbar = false;
  showCheckboxDeselectAllToolbar = false;
  // IMPORTANT: always add the + symbol before any number passed to checked, otherwise it will interpret it like a string, resulting in an error
  checked: Map<number, boolean> = null;   // number = student's id (serial)

  selectedStudentToAdd: Student = null;
  private urlParamSubscription: Subscription;

  // TODO: temp for images, delete later
  flag = 'https://upload.wikimedia.org/wikipedia/commons/9/9d/Flag_of_Arkansas.svg';

  constructor(private route: ActivatedRoute) {
    this.urlParamSubscription = this.route.parent.url.subscribe(() => {
      // Every time I change the route, I make sure the autocomplete input data is updated.. (setAutocompleteInit)
      delete this.filteredOptions$;
      // Needed to reset the input of autocomplete, after a route change
      this.autocompleteControl.reset(null);
    });
  }
  ngOnInit() {
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  // Data and sort update done automatically in '@Input() set enrolled'
  studentsAdd() {
    if (this.selectedStudentToAdd && this.students.includes(this.selectedStudentToAdd)
      && !this.enrolled.includes(this.selectedStudentToAdd)) {
      this.enrolledEvent.emit([this.selectedStudentToAdd]);
      this.checked.set(this.selectedStudentToAdd.id, false);
      this.selectedStudentToAdd = null;
    }
  }
  studentsRemove() {
    const selectedStudentToRemove = this.enrolled.filter(x => this.checked.get(+x.id));
    this.disenrolledEvent.emit(selectedStudentToRemove);
    this.checked = new Map(this.enrolled.map(x => [x.id, false]));
  }
  sortChange(sort: Sort) {
    this.sort = sort as MatSort;
    this.sortData();
  }
  // changes dataSource.data, which is linked by setter to the template component. As soon as data gets updated, sort is called again (in enrolled setter)
  sortData() {
    if (!this.sort || !this.sort.active || this.sort.direction === '') {
      return;
    }
    this.dataSource.data = [...this.dataSource.data].sort((a, b) => {
      const isAsc = !(this.sort.direction === 'asc'); // ASC: the sorting array (GUI) is more intuitive this way
      return sortCompare(a[this.sort.active], b[this.sort.active], isAsc);
    });
    this.updateMasterCheckbox();
  }
  // checkbox logic
  private getStudentsIdCurrentPage(): number[] {
    let endIndex: number;
    const arr: number[] = [];
    if (this.dataSource.data.length > (this.dataSource.paginator.pageIndex + 1) * this.dataSource.paginator.pageSize) {
      endIndex = (this.dataSource.paginator.pageIndex + 1) * this.dataSource.paginator.pageSize;
    } else {
      endIndex = this.dataSource.data.length;
    }
    for (let index: number = (this.dataSource.paginator.pageIndex * this.dataSource.paginator.pageSize), count = 0; index < endIndex; index++, count++) {
      arr[count] = +this.dataSource.data[+index].id;
    }
    return arr;
  }
  // change selection, sort update, paginator update
  private updateMasterCheckbox() {
    const idStudentsPage: [number, boolean][] = [...this.checked.entries()]
      .filter(x => this.getStudentsIdCurrentPage().includes(x[0], 0));
    const checkboxesPage: boolean[] = idStudentsPage
      .map(x => x[1]);
    this.checkboxMasterCompleted = checkboxesPage.every(t => t === true);
    this.checkboxMasterIndeterminate = !this.checkboxMasterCompleted && idStudentsPage.filter(x => x[1] === true).length > 0;
  }
  checkboxChangeSelection({checked}, id) {
    this.checked.set(+id, checked);
    this.updateMasterCheckbox();
  }
  checkboxIsChecked(id: number) {
    return this.checked.get(+id);
  }
  checkboxSetAll(completed: boolean) {
    [...this.checked.entries()].forEach(t => this.checked.set(+t[0], completed));
    this.checkboxMasterCompleted = completed;
    this.checkboxMasterIndeterminate = false;
    this.showCheckboxDeselectAllToolbar = completed;
    this.showCheckboxSelectAllToolbar = false;
  }
  checkboxSetAllPage(completed: boolean) {
    for (const id of this.getStudentsIdCurrentPage()) {
      this.checked.set(+id, completed);
    }
    this.checkboxMasterCompleted = completed;
    this.checkboxMasterIndeterminate = false;

    if (completed === true && this.dataSource.data.length > [...this.checked.values()].filter(value => value === true).length) {
      this.showCheckboxSelectAllToolbar = true;
      this.showCheckboxDeselectAllToolbar = false;
    } else {
      this.showCheckboxSelectAllToolbar = false;
      this.showCheckboxDeselectAllToolbar = false;
    }
  }
  autocompleteDisplayFunction(student: Student): string {
    return student && student.firstName && student.lastName && student.id ? student.firstName + ' ' + student.lastName : '';
  }
  autocompleteSaveOption(event: MatAutocompleteSelectedEvent) {
    this.selectedStudentToAdd = (event).option.value;
  }
  paginatorUpdate() {
    this.updateMasterCheckbox();
    this.showCheckboxSelectAllToolbar = false;
    this.showCheckboxDeselectAllToolbar = false;
  }
  ngOnDestroy(): void {
    this.urlParamSubscription.unsubscribe();
  }
}

function sortCompare(a: number | string, b: number | string, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
