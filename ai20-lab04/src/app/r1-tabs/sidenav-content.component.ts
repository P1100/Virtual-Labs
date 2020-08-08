import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Course} from '../models/course.model';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

const DB_COURSES: Course[] = [
  {id: 1, label: 'Applicazioni Internet', path: 'applicazioni-internet', fullName: '', minEnrolled: 0, maxEnrolled: 0, enabled: true},
  {
    id: 2,
    label: 'Programmazione di sistema',
    path: 'programmazione-di-sistema',
    fullName: '',
    minEnrolled: 0,
    maxEnrolled: 0,
    enabled: true
  },
  {id: 3, label: 'Mobile development', path: 'mobile-development', fullName: '', minEnrolled: 0, maxEnrolled: 0, enabled: true}
];
//   .map(y => {
//   y.path = 'teacher/course/' + y.path;
//   return y;
// });
const tabs = [
  {path: 'students', label: 'Students'},
  {path: 'vms', label: 'VMs'},
  {path: 'groups', label: 'Groups'},
  {path: 'assignments', label: 'Assignments'}
];

@Component({
  selector: 'app-sidenav-cont',
  templateUrl: './sidenav-content.component.html',
  styleUrls: ['../../_unused/sidenav-content.component.css']
})
export class SidenavContentComponent implements OnInit, OnChanges, OnDestroy {
  prefix = '/teacher';
  navLinks = [];
  // TODO: need to get this value for a course service or routing
  activeCourse = 1;
  paramSubscription: Subscription;

  constructor(private route: ActivatedRoute) {
    // Devo riaggiornare i tabs ad ogni cambio di corso. Versione no observable =>  this.route.snapshot.paramMap.get("id");
    this.paramSubscription = this.route.url.subscribe(url => {
      this.activeCourse = +this.route.snapshot.paramMap.get('id');
      this.navLinks = [];
      for (const tab of tabs) {
        this.navLinks.push({path: this.prefix + '/course/' + this.activeCourse + '/' + tab.path, label: tab.label});
      }
      // console.log('Sidenav-cont.constructor route.url activeCourse: ' + '\n' + this.activeCourse);
    });
    // console.log('Sidenav-cont.constructor ending Routes:\n' + JSON.stringify(this.navLinks));
  }
  ngOnInit(): void {
    // console.log('Sidenav-cont.ngOnInit');
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\nSidenavContentComponent.ngOnChanges - activerCourse:\n' + this.activeCourse);
  }
  ngOnDestroy(): void {
    console.log('Sidenav-cont.ngOnDestroy');
    this.paramSubscription.unsubscribe();
  }
}
