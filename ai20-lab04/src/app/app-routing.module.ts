import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EmptyComponent} from './r1-content/empty.component';
import {VmsContComponent} from './r2-inner-tab/vms/vms-cont.component';
import {StudentsContComponent} from './r2-inner-tab/students/students-cont.component';
import {PageNotFoundComponent} from './r0-topheader-leftsidebar/page-not-found.component';
import {AssignmentsContComponent} from './r2-inner-tab/assignments/assignments-cont.component';
import {TabsMenuComponent} from './r1-content/tabs-menu.component';
import {GroupsContComponent} from './r2-inner-tab/groups/groups-cont.component';
import {HomeComponent} from './r0-topheader-leftsidebar/home.component';

const routes: Routes = [
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
            children: [
              {
                path: ':id',
                component: TabsMenuComponent,
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
                    path: 'groups',
                    component: GroupsContComponent
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
  imports: [RouterModule.forRoot(routes, {enableTracing: false, onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
