import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Course} from '../model/course.model';
import {ActivatedRoute} from '@angular/router';

const DB_COURSES: Course[] = [
  {id: 1, label: 'Applicazioni Internet', path: 'applicazioni-internet'},
  {id: 2, label: 'Programmazione di sistema', path: 'programmazione-di-sistema'},
  {id: 3, label: 'Mobile development', path: 'mobile-development'}
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

// TODO: da rivedere, anche in base al progetto
const DBEs2pdf = {
  students: [{
    id: 1, serial: '265373', name: 'ALICINO', firstName:
      'GIUSEPPE', courseId: 1, groupId: 0
  }],
  courses: [{id: 1, name: 'Applicazioni Internet', path: 'applicazioniinternet'}],
  groups: [{id: 1, name: 'Calvary'}]
};

@Component({
  selector: 'app-sidenav-cont',
  templateUrl: './sidenav-cont.component.html',
  styleUrls: ['./sidenav-cont.component.css']
})
export class SidenavContComponent implements OnInit, OnChanges {
  @Input()
  title: string;
  courses = DB_COURSES;
  isTeacher = true;
  prefix = '';
  navLinks = [];
  // TODO: need to get this value for a course service or routing
  activeCourse = 1;

  constructor(private route: ActivatedRoute) {
    console.log('SidenavContComponent.constructor Title:\n' + this.title);
    if (this.isTeacher === false) {
      this.prefix = '/student';
    } else {
      this.prefix = '/teacher';
    }
    // Devo riaggiornare i tabs ad ogni cambio di corso
    this.route.url.subscribe(url => {
      this.activeCourse = +this.route.snapshot.paramMap.get('id');
      this.navLinks = [];
      for (const tab of tabs) {
        this.navLinks.push({path: this.prefix + '/course/' + this.activeCourse + '/' + tab.path, label: tab.label});
      }
      console.log('@Sidenav-cont route.url: ' + url.toString() + '-' + this.activeCourse);
    });

    console.log('SidenavContComponent.constructor ending Routes:\n' + JSON.stringify(this.navLinks));
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('SidenavContComponent.ngOnChanges - activerCourse:\n' + this.activeCourse);
    // throw new Error('Method not implemented.');
  }
}
