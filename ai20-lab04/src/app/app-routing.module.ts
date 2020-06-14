import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {StudentsContComponent} from './teacher/students-cont/students-cont.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {AssignmentsContComponent} from './teacher/assignments-cont/assignments-cont.component';
import {GroupsContComponent} from './teacher/groups-cont/groups-cont.component';
import {VmsContComponent} from './teacher/vms-cont/vms-cont.component';
import {SidenavContComponent} from './home/sidenav-cont.component';
import {AuthGuard} from './auth/auth.guard';
import {HomeTabComponent} from './home/home-tab.component';

// TODO: use this.router.navigate(['teacher','course'])
// this.router.navigateByUrl(`/courses/${course.id}`);
//     this.router.navigate(['/courses',course.id]);
const routes: Routes = [
  {
    path: '',
    // component: HomeComponent,
    // resolve: {
    //   profile: ProfileResolver
    // },
    // canDeactivate: [CanDeactivateGuard],
    // resolve: {
    //   crisis: CrisisDetailResolverService
    // }
    children: [
      {
        path: 'home',
        component: HomeTabComponent
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
            canActivate: [AuthGuard],
            canActivateChild: [AuthGuard],
            children: [
              {
                path: ':id',
                component: SidenavContComponent,
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
  // /student/course/:courseId/vms
  // {path: 'teacher/course/applicationi-internet/assignments', component: AssignmentsContComponent},
  // {path: 'mobile-development', component: OtherCourseComponent},
  // {path: 'home', redirectTo: '/', pathMatch: 'full'}, // redirect to `first-component`, which is HomeComponent
  {path: '**', component: PageNotFoundComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: false})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
