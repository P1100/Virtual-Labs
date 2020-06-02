import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {StudentsContComponent} from './teacher/students-cont/students-cont.component';
import {OtherCourseComponent} from './other-course/other-course.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {HomeComponent} from './home/home.component';
import {AssignmentsContComponent} from './assignments-cont/assignments-cont.component';
import {GroupsContComponent} from './groups-cont/groups-cont.component';
import {VmsContComponent} from './vms-cont/vms-cont.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'teacher/course/applicationi-internet/students', component: StudentsContComponent},
  {path: 'teacher/course/applicationi-internet/vms', component: VmsContComponent},
  {path: 'teacher/course/applicationi-internet/groups', component: GroupsContComponent},
  {path: 'teacher/course/applicationi-internet/assignments', component: AssignmentsContComponent},
  {path: 'programmazione-di-sistema', component: OtherCourseComponent},
  {path: 'mobile-development', component: OtherCourseComponent},
  // { path: '',   redirectTo: '/applicazioni-internet', pathMatch: 'full' }, // redirect to `first-component`
  {path: '**', component: PageNotFoundComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModuleModule {
}
