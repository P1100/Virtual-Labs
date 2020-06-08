import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from '../_unused/app/app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {LayoutModule} from '@angular/cdk/layout';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {StudentsComponent} from './teacher/students-cont/students/students.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCardModule} from '@angular/material/card';
import {MatMenuModule} from '@angular/material/menu';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {FormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {StudentsContComponent} from './teacher/students-cont/students-cont.component';
import {MainRoutingModule} from './main-routing.module';
import {OtherCourseComponent} from './other-course/other-course.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {VmsContComponent} from './teacher/vms-cont/vms-cont.component';
import {GroupsContComponent} from './teacher/groups-cont/groups-cont.component';
import {AssignmentsContComponent} from './teacher/assignments-cont/assignments-cont.component';
import {HomeComponent} from './home/home.component';

// ToDO: Passare a SSL nel progetto finale (?!)
// Removed Ivy support, in ./tsconfig.json, for compatibility with augury
@NgModule({
  declarations: [
    AppComponent,
    StudentsComponent,
    StudentsContComponent,
    OtherCourseComponent,
    PageNotFoundComponent,
    VmsContComponent,
    GroupsContComponent,
    AssignmentsContComponent,
    HomeComponent
  ],
  imports: [
    MatInputModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    LayoutModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    DragDropModule,
    MatGridListModule,
    MatCardModule,
    MatMenuModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatCheckboxModule,
    FormsModule,
    MainRoutingModule
  ],
  providers: [],
  bootstrap: [HomeComponent]
})
export class MainModule {
}
