import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EmptyComponent} from './r1-tabs/empty.component';
import {VmsContComponent} from './tabs/vms/vms-cont.component';
import {StudentsContComponent} from './tabs/students/students-cont.component';
import {PageNotFoundComponent} from './tabs/page-not-found/page-not-found.component';
import {AssignmentsContComponent} from './tabs/assignments/assignments-cont.component';
import {SidenavContentComponent} from './r1-tabs/sidenav-content.component';
import {GroupsContComponent} from './tabs/groups/groups-cont.component';

const routes: Routes = [
  {
    path: '',
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
            component: PageNotFoundComponent
          },
          {
            path: 'course',
// TODO: reactivate Auth logic later (look es5 commit to be sure, not everything is to uncomment...)
            // canActivate: [AuthGuard],
            // canActivateChild: [AuthGuard],
            children: [
              {
                path: ':id',
                component: SidenavContentComponent,
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
  // {path: 'home', redirectTo: '/', pathMatch: 'full'}, // redirect to `first-component`, which is HomeComponent
  {path: '**', component: PageNotFoundComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: false, onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

// this.router.navigate(['teacher','course'])
//     this.router.navigate(['/courses',course.id]);
// this.router.navigateByUrl(`/courses/${course.id}`);
