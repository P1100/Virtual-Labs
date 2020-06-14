import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VmsContComponent} from '../tabs/vms-cont/vms-cont.component';
import {StudentsContComponent} from '../tabs/students-cont.component';
import {SidenavContComponent} from './home/sidenav-cont.component';
import {PageNotFoundComponent} from '../tabs/page-not-found/page-not-found.component';
import {HomeRouteTabComponent} from './home/home-route-tab.component';
import {AuthGuard} from './auth/auth.guard';
import {AssignmentsContComponent} from '../tabs/assignments-cont/assignments-cont.component';
import {GroupsContComponent} from '../tabs/groups-cont/groups-cont.component';

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
        component: HomeRouteTabComponent
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
