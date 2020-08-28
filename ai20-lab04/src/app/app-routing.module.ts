import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EmptyComponent} from './r1-content-tabs/empty.component';
import {VmsContComponent} from './r2-inner-tab/vms/vms-cont.component';
import {StudentsContComponent} from './r2-inner-tab/students/students-cont.component';
import {PageNotFoundComponent} from './r0-topheader-leftsidebar/page-not-found.component';
import {AssignmentsContComponent} from './r2-inner-tab/assignments/assignments-cont.component';
import {TabsNavComponent} from './r1-content-tabs/tabs-nav.component';
import {HomeComponent} from './r0-topheader-leftsidebar/home.component';
import {TestingComponent} from './r2-inner-tab/testing/testing.component';
import {TeamsComponent} from './r2-inner-tab/teams/teams.component';

const routes: Routes = [
  {
    path: 'testing',
    component: TestingComponent
  },
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'home',
        component: EmptyComponent
      },
      {
        path: 'teacher',
        children: [
          {
            path: '',
            component: EmptyComponent
          },
          {
            path: 'course',
// TODO: reactivate Auth logic later (look es5 commit to be sure, not everything is to uncomment...)
            // canActivate: [AuthGuard],
            // canActivateChild: [AuthGuard],
            // canLoad: []
            children: [
              {
                path: ':id',
                component: TabsNavComponent,
                children: [
                  {
                    path: '',
                    component: StudentsContComponent,
                    pathMatch: 'full'
                  },
                  {
                    path: 'students',
                    component: StudentsContComponent
                  },
                  {
                    path: 'vms',
                    component: VmsContComponent
                  },
                  {
                    path: 'teams',
                    component: TeamsComponent
                  },
                  {
                    path: 'assignments',
                    component: AssignmentsContComponent
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
  },
  {path: '**', component: PageNotFoundComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: false, onSameUrlNavigation: 'ignore', paramsInheritanceStrategy: 'always'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
