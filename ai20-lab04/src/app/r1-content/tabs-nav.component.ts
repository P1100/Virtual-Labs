import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {tabs} from '../app-settings';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs-nav.component.html',
  styleUrls: ['./tabs-nav.component.css']
})
export class TabsNavComponent implements OnInit, OnChanges, OnDestroy {
  // TODO: questo valore deve essere impostato dalla logica di auth
  prefix = ['/teacher', '/student'];
  navLinks = [];
  // TODO: need to get this value for a course service or routing
  activeCourse = null;
  paramSubscription: Subscription;

  constructor(private route: ActivatedRoute) {
    // Devo riaggiornare i tabs ad ogni cambio di corso. Versione no observable =>  this.route.snapshot.paramMap.get("id");
    this.paramSubscription = this.route.url.subscribe(() => {
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
    console.log('°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\nTabsNavComponent.ngOnChanges - activerCourse:\n' + this.activeCourse);
  }
  ngOnDestroy(): void {
    console.log('Sidenav-cont.ngOnDestroy');
    this.paramSubscription.unsubscribe();
  }
}
