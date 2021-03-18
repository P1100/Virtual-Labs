import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {AppSettings} from '../app-settings';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs-nav.component.html',
  styleUrls: ['./tabs-nav.component.css']
})
export class TabsNavComponent implements OnDestroy {
  prefix = ['professor', 'student'];
  navLinks = [];
  activeCourse = null;
  paramSubscription: Subscription;

  constructor(private route: ActivatedRoute) {
    // Update tabs link url at every course change
    this.paramSubscription = this.route.url.subscribe(() => {
      this.prefix = [localStorage.getItem('role')];
      this.activeCourse = this.route.snapshot.paramMap.get('id');
      this.navLinks = [];
      const tabs = localStorage.getItem('role') == 'professor' ? AppSettings.tabsProfessor : AppSettings.tabsStudent;
      for (const tab of tabs) {
        this.navLinks.push({path: '/' + this.prefix[0] + '/course/' + this.activeCourse + '/' + tab.path, label: tab.label});
      }
    });
  }
  ngOnDestroy(): void {
    this.paramSubscription.unsubscribe();
  }
}
