// ToDO: Passare a SSL nel progetto finale (?!)
// Removed Ivy support, in ./tsconfig.json, for compatibility with augury

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {HomeComponent, LoginDialogReactiveComponent, LoginDialogTemplateComponent} from './home/home.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {AuthInterceptor} from './auth/auth.interceptor';
import {NgModule} from '@angular/core';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatMenuModule} from '@angular/material/menu';
import {MatGridListModule} from '@angular/material/grid-list';
import {AppRoutingModule} from './app-routing.module';
import {MatSortModule} from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatCardModule} from '@angular/material/card';
import {StudentsComponent} from './tabs/students/students.component';
import {MatTabsModule} from '@angular/material/tabs';
import {PageNotFoundComponent} from './tabs/page-not-found/page-not-found.component';
import {MatButtonModule} from '@angular/material/button';
import {HomeRouteTabComponent} from './home/home-route-tab.component';
import {GroupsContComponent} from './tabs/groups-cont/groups-cont.component';
import {LayoutModule} from '@angular/cdk/layout';
import {VmsContComponent} from './tabs/vms-cont/vms-cont.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AssignmentsContComponent} from './tabs/assignments-cont/assignments-cont.component';
import {BrowserModule} from '@angular/platform-browser';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {StudentsContComponent} from './tabs/students/students-cont.component';
import {AppComponent} from '../_unused/app/app.component';
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from '@angular/material/dialog';
import {SidenavContComponent} from './home/sidenav-cont.component';
import {OtherCourseComponent} from './tabs/other-course/other-course.component';

@NgModule({
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
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDialogModule
  ],
  declarations: [
    AppComponent,
    StudentsComponent,
    StudentsContComponent,
    OtherCourseComponent,
    PageNotFoundComponent,
    VmsContComponent,
    GroupsContComponent,
    AssignmentsContComponent,
    HomeComponent,
    SidenavContComponent,
    LoginDialogTemplateComponent,
    LoginDialogReactiveComponent,
    HomeRouteTabComponent
  ],
  entryComponents: [
    HomeComponent, LoginDialogTemplateComponent, LoginDialogReactiveComponent
  ],
  providers: [HttpClientModule, {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [HomeComponent]
})
export class AppModule {
}