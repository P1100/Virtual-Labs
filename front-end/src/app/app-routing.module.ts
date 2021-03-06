import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WelcomeEmptyComponent} from './r0-topheader-leftsidebar/welcome-empty.component';
import {VmsStudContComponent} from './r2-inner-tab/student/vms/vms-stud-cont.component';
import {StudentsContComponent} from './r2-inner-tab/professor/students/students-cont.component';
import {PageNotFoundComponent} from './r0-topheader-leftsidebar/page-not-found.component';
import {TabsNavComponent} from './r1-tabs-menu/tabs-nav.component';
import {HomeComponent} from './r0-topheader-leftsidebar/home.component';
import {TestingComponent} from './r2-inner-tab/testing/testing.component';
import {UnauthorizedComponent} from './r0-topheader-leftsidebar/unauthorized.component';
import {AuthStudentGuard} from './services/auth-student.guard';
import {AuthProfessorGuard} from './services/auth-professor.guard';
import {UnauthorizedTabComponent} from './r0-topheader-leftsidebar/unauthorized-tab.component';
import {TeamsContComponent} from './r2-inner-tab/student/teams/teams-cont.component';
import {VmsProfContComponent} from './r2-inner-tab/professor/vms/vms-prof-cont.component';
import {AssignmentStudContComponent} from './r2-inner-tab/student/assignment-stud/assignment-stud-cont.component';
import {AssignmentProfContComponent} from './r2-inner-tab/professor/assignment-prof/assignment-prof-cont.component';

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
        path: '',
        component: WelcomeEmptyComponent,
        pathMatch: 'full'
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
                    component: VmsProfContComponent
                  },
                  {
                    path: 'teams',
                    component: UnauthorizedTabComponent
                  },
                  {
                    path: 'assignments',
                    component: AssignmentProfContComponent
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
                    component: TeamsContComponent, // Default view for professor
                    pathMatch: 'full'
                  },
                  {
                    path: 'students',
                    component: UnauthorizedTabComponent
                  },
                  {
                    path: 'vms',
                    component: VmsStudContComponent
                  },
                  {
                    path: 'teams',
                    component: TeamsContComponent
                  },
                  {
                    path: 'assignments',
                    component: AssignmentStudContComponent
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
      },
      {
        path: ':string',
        component: WelcomeEmptyComponent,
        pathMatch: 'full'
      }
    ]
  },
  {path: '**', component: PageNotFoundComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: false,
    onSameUrlNavigation: 'reload',
    paramsInheritanceStrategy: 'always',
    relativeLinkResolution: 'corrected'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
