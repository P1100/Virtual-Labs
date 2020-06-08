import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Course} from '../course';

const DB_COURSES: Course[] = [
  {id: 1, label: 'Applicazioni Internet', path: 'applicazioni-internet'},
  {id: 2, label: 'Programmazione di sistema', path: 'programmazione-di-sistema'},
  {id: 3, label: 'Mobile development', path: 'mobile-development'}
];
const tabs = [
  {path: 'students', label: 'Students'},
  {path: 'vms', label: 'VMs'},
  {path: 'groups', label: 'Groups'},
  {path: 'assignments', label: 'Assignments'}
];
//   .map(y => {
//   y.path = 'teacher/course/' + y.path;
//   return y;
// });

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
  // selector changed from app-root, inserted in index.html!
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnChanges {
  title = 'VirtualLabs';
  courses = DB_COURSES;
  isTeacher = true;
  prefix = '';
  navLinks = [];
  activeCourse: any;

  constructor() {
    // this.activeCourse = this.courses[1].path;
  }
  ngOnChanges(changes: SimpleChanges): void {
    console.log('##################################');
    console.log('##################################');
    console.log('activerCourse=' + this.activeCourse);
    // throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    console.log('##################################');
    console.log('##################################');
    console.log('On init: ' + this.title);
    if (this.isTeacher === true) {
      this.prefix = 'student';
    } else {
      this.prefix = 'teacher';
    }
    for (const course of this.courses) {
      for (const tab of tabs) {
        this.navLinks.push({path: this.prefix + '/course/' + course.path + '/' + tab.path, label: tab.label});
      }
    }
    console.log(this.navLinks);
  }

}
