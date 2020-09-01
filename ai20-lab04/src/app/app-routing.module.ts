import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WelcomeEmptyComponent} from './r1-tabs-menu/welcome-empty.component';
import {VmsContComponent} from './r2-inner-tab/vms/vms-cont.component';
import {StudentsContComponent} from './r2-inner-tab/professor/students/students-cont.component';
import {PageNotFoundComponent} from './r0-topheader-leftsidebar/page-not-found.component';
import {AssignmentsContComponent} from './r2-inner-tab/assignments/assignments-cont.component';
import {TabsNavComponent} from './r1-tabs-menu/tabs-nav.component';
import {HomeComponent} from './r0-topheader-leftsidebar/home.component';
import {TestingComponent} from './r2-inner-tab/testing/testing.component';
import {TeamsComponent} from './r2-inner-tab/teams/teams.component';
import {UnauthorizedComponent} from './r0-topheader-leftsidebar/unauthorized.component';
import {AuthStudentGuard} from './services/auth-student.guard';
import {AuthProfessorGuard} from './services/auth-professor.guard';
import {UnauthorizedTabComponent} from './r0-topheader-leftsidebar/unauthorized-tab.component';

const routes: Routes = [
  {
    path: 'testing',
    component: TestingComponent
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'home',
        component: WelcomeEmptyComponent
      },
      {
        path: 'professor',
        canActivateChild: [AuthProfessorGuard],
        children: [
          {
            path: '',
            component: WelcomeEmptyComponent
          },
          {
            path: 'course',
            children: [
              {
                path: ':id',
                component: TabsNavComponent,
                children: [
                  {
                    path: '',
                    component: StudentsContComponent, // Default view for professor
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
                    component: UnauthorizedTabComponent
                  },
                  {
                    path: 'assignments',
                    component: AssignmentsContComponent
                  },
                  {
                    path: '**',
                    component: UnauthorizedComponent
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        path: 'student',
        canActivateChild: [AuthStudentGuard],
        children: [
          {
            path: '',
            component: WelcomeEmptyComponent
          },
          {
            path: 'course',
            children: [
              {
                path: ':id',
                component: TabsNavComponent,
                children: [
                  {
                    path: '',
                    component: WelcomeEmptyComponent
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
                  },
                  {
                    path: '**',
                    component: UnauthorizedTabComponent
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
