import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Course} from '../models/course.model';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {BackendService} from '../services/backend.service';

// Costruzione dinamica delle tabs parte da qui
const tabs = [
  {path: 'students', label: 'Students'},
  {path: 'vms', label: 'VMs'},
  {path: 'groups', label: 'Groups'},
  {path: 'assignments', label: 'Assignments'}
];

@Component({
  selector: 'app-sidenav-cont',
  templateUrl: './sidenav-content.component.html',
  styleUrls: []
})
export class SidenavContentComponent implements OnInit, OnChanges, OnDestroy {
  // TODO: questo valore deve essere impostato dalla logica di auth
  prefix = ['/teacher', '/student'];
  navLinks = [];
  // TODO: need to get this value for a course service or routing
  activeCourse = null;
  paramSubscription: Subscription;

  courses: Course[] = null;

  constructor(private route: ActivatedRoute, private backendService: BackendService) {
    // Primo e unico aggiornamento corsi. Altri updates solo nelle funzioni di modifica
    this.backendService.getCourses().subscribe(x => this.courses = [...x]);
    // Devo riaggiornare i tabs ad ogni cambio di corso. Versione no observable =>  this.route.snapshot.paramMap.get("id");
    this.paramSubscription = this.route.url.subscribe(url => {
      this.activeCourse = +this.route.snapshot.paramMap.get('id');
      this.navLinks = [];
      for (const tab of tabs) {
        this.navLinks.push({path: this.prefix[0] + '/course/' + this.activeCourse + '/' + tab.path, label: tab.label});
      }
      // console.log('Sidenav-cont.constructor route.url activeCourse: ' + '\n' + this.activeCourse);
    });
    // console.log('Sidenav-cont.constructor ending Routes:\n' + JSON.stringify(this.navLinks));
  }
  ngOnInit(): void {
    // this.backendService.getCourses().subscribe(value => console.log(JSON.stringify(value)));
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
