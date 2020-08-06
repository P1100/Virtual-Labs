import {AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {Student} from '../../models/student.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
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
// Non vengono usati Observables in questa classe, perché tutta la comunicazione asincrona con server/servizio avviene nel componente container
export class StudentsComponent implements OnInit, AfterViewInit, OnDestroy {
  @Output('enrolledEvent')
  enrolledEvent = new EventEmitter<Student[]>();
  @Output('disenrolledEvent')
  disenrolledEvent = new EventEmitter<Student[]>();
  // Needed to get paginator instance
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // Needed sort variable
  @ViewChild(MatSort) sort: MatSort;
  // displayedColumnsTable is based on Student class (manual synch), controls various formatting elements
  displayedColumnsTable: string[] = ['select', 'id', 'firstName', 'lastName', 'email', 'group'];
  // MatPaginator Inputs
  length: number; // The current total number of items being paged. Read only
  pageSize = 25;
  pageSizeOptions: number[] = [1, 2, 5, 10, 20];
  myControl = new FormControl();
  // Table data, collegata direttamente a students enrolled tramite setter/getter (binding diretto di enrolledStudents dal padre)
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  selectedStudentToAdd: Student = null;
  // Checkbox
  checked: Map<number, boolean> = null;
  allCompletePage = false;
  // Used in AutoComplete. It's the list of all students but at times filtered (so cant be merged in only one var)
  filteredOptions$: Observable<Student[]>;
  // Course id of current page
  public id: string;
  // Checkbox dialog gmail style
  selectAllDialog: boolean = false;
  removeAllDialog: boolean = false;
  // Private variable used to get data from the service (superset of filteredOptions, used in autocomplete)
  @Input()
  private students: Student[];
  private paramSubscription: Subscription;
  constructor(private route: ActivatedRoute) {
    this.paramSubscription = this.route.parent.url.subscribe(url => {
      this.id = this.route.parent.snapshot.paramMap.get('id');
    });
    // this.id = this.route.snapshot.paramMap.get('id');
    // console.log('# students.constructor selectedStudentToAdd:\n' + this.selectedStudentToAdd);
  }
  get enrolled(): Student[] {
    return this.dataSource.data;
  }
  @Input()
  set enrolled(array: Student[]) {
    this.dataSource.data = [...array];
    this.sortData();
    array.map(x => console.log('typeof arrayEnrolled.map', x.id, typeof x.id, +x.id, typeof (+x.id)));
    this.checked = new Map(array.map(x => [+x.id, false]));
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
      //   if (this.masterStatus === 1) {
      //     this.masterStatus = 2;
      //   }
    }
  }
  // Passing the students to remove (whole Students array, not just the id)
  studentsRemove() {
    const selectedStudentToRemove = this.enrolled.filter(x => this.checked.get(x.id));
    this.disenrolledEvent.emit(selectedStudentToRemove);
    this.checked = new Map(this.enrolled.map(x => [x.id, false]));
    // this.checkedCount = 0;
    // this.masterStatus = 0;
  }
  sortChange(sort: MatSort) {
    // console.log('# students.sortChange selectedStudentToAdd: ' + JSON.stringify(this.selectedStudentToAdd));
    this.sort = sort;
    this.sortData();
  }
  // changes dataSource.data, which is linked by setter to the template component. As soon as data gets updated, sort is called again (look enrolled setter)
  sortData() {
    if (!this.sort || !this.sort.active || this.sort.direction === '') {
      return;
    }
    this.dataSource.data = [...this.dataSource.data].sort((a, b) => {
      const isAsc = !(this.sort.direction === 'asc'); // the sorting array (GUI) is more intuitive this way
      // console.log(this.sort.active);
      return sortCompare(a[this.sort.active], b[this.sort.active], isAsc);
    });
  }
  checkboxChangeSelection({checked}, id) {
    console.log('checkboxChangeSelection[id][checked]: ' + id + checked + typeof id);
    this.checked.set(+id, checked);
    this.allCompletePage = [...this.checked.values()].every(t => t === true);
  }
  checkboxSomeComplete(): boolean {
    // console.log('checkboxSomeComplete[return boolean]: ' + ([...this.checked.values()].filter(t => t === true).length > 0 && !this.allCompletePage));
    // console.log(this.allCompletePage);
    // console.log(this.checked);
    return [...this.checked.values()].filter(t => t === true).length > 0 && !this.allCompletePage;
  }
  checkboxIsChecked(id: number) {
    // console.log('checkboxIsChecked[id][checked.get[id]]: ' + id + ' - ' + this.checked.get(id) + typeof id);
    return this.checked.get(+id);
  }
  checkboxSetAll(completed: boolean) {
    [...this.checked.entries()].forEach(t => this.checked.set(+t[0], completed));
    this.allCompletePage = completed;
    this.selectAllDialog = !completed && (this.removeAllDialog === false);
    this.removeAllDialog = completed;
  }
  checkboxSetAllPage(completed: boolean) {
    let endIndex: number;
    console.log('length', this.dataSource.data.length);
    console.log('pageIndex', this.dataSource.paginator.pageIndex);
    console.log('pageSize', this.dataSource.paginator.pageSize);
    if (this.dataSource.data.length > (this.dataSource.paginator.pageIndex + 1) * this.dataSource.paginator.pageSize) {
      endIndex = (this.dataSource.paginator.pageIndex + 1) * this.dataSource.paginator.pageSize;
    } else {
      endIndex = this.dataSource.data.length;
    }
    for (let index: number = (this.dataSource.paginator.pageIndex * this.dataSource.paginator.pageSize); index < endIndex; index++) {
      console.log('typedof index', index, typeof index, endIndex);
      this.checked.set(+this.dataSource.data[+index].id, completed);
      console.log('his.dataSource.data[+index]', this.dataSource.data[+index]);
    }
    // [...this.checked.entries()].forEach(t => this.checked.set(t[0], completed));
    this.allCompletePage = true;
    // console.log('checkboxSetAll: ' + completed);
    // console.log([...this.checked.entries()]);
    console.log(this.checked);
    if (this.dataSource.data.length > this.paginator.pageSize) {
      if (completed === true) {
        this.selectAllDialog = true;
        this.removeAllDialog = false;
      }
    } else {
      this.selectAllDialog = false;
      this.removeAllDialog = true;
    }
    // console.log([...this.checked.entries()]);
    // console.log(this.checked);
  }
  autocompleteDisplayFunction(student: Student): string {
    return student && student.firstName && student.lastName && student.id ? student.firstName + ' ' + student.lastName : '';
  }
  autocompleteSaveOption(event: MatAutocompleteSelectedEvent) {
    this.selectedStudentToAdd = (event).option.value;
    // console.log('# StudentsComponent.autocompleSave added student' + JSON.stringify(this.selectedStudentToAdd));
  }
  ngOnDestroy(): void {
    this.paramSubscription.unsubscribe();
  }
// delete later, if things works without issues
  // @ViewChild(MatSidenav) matsidenav: MatSidenav;
}

function sortCompare(a: number | string, b: number | string, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
