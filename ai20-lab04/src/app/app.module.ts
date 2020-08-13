import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {HomeComponent} from './r0-topheader-leftsidebar/home.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {ProfileComponent} from './dialogs/profile/profile.component';
import {AuthInterceptor} from './auth/auth.interceptor';
import {LoginComponent} from './dialogs/login/login.component';
import {NgModule} from '@angular/core';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppRoutingModule} from './app-routing.module';
import {MatSortModule} from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatCardModule} from '@angular/material/card';
import {StudentsComponent} from './r2-inner-tab/students/students.component';
import {MatTabsModule} from '@angular/material/tabs';
import {PageNotFoundComponent} from './r0-topheader-leftsidebar/page-not-found.component';
import {GroupsContComponent} from './r2-inner-tab/groups/groups-cont.component';
import {TabsMenuComponent} from './r1-content/tabs-menu.component';
import {VmsContComponent} from './r2-inner-tab/vms/vms-cont.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {MatIconModule} from '@angular/material/icon';
import {TestDialogComponent} from './dialogs/test-dialog/test-dialog.component';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AssignmentsContComponent} from './r2-inner-tab/assignments/assignments-cont.component';
import {EditRemoveCourseComponent} from './dialogs/edit-remove-course/edit-remove-course.component';
import {BrowserModule} from '@angular/platform-browser';
import {AreYouSureComponent} from './dialogs/are-you-sure/are-you-sure.component';
import {EmptyComponent} from './r1-content/empty.component';
import {StudentsContComponent} from './r2-inner-tab/students/students-cont.component';
import {AppComponent} from './app.component';
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from '@angular/material/dialog';
import {RegisterComponent} from './dialogs/register/register.component';
import {MatButtonModule} from '@angular/material/button';
import {MatMenuModule} from '@angular/material/menu';

@NgModule({
  imports: [
    MatInputModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatTabsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatCardModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatCheckboxModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    // Removing these wont generate an error, but GUI wont display properly
    MatDialogModule,
    MatButtonModule,
    MatMenuModule
    /* Unused */
    // LayoutModule,
    // DragDropModule,
    // MatGridListModule,
    /* -- Dont import below, unless for test */
    // RouterTestingModule,
    // HttpClientTestingModule
  ],
  declarations: [
    AppComponent,
    StudentsComponent,
    StudentsContComponent,
    PageNotFoundComponent,
    VmsContComponent,
    GroupsContComponent,
    AssignmentsContComponent,
    HomeComponent,
    TabsMenuComponent,
    TestDialogComponent,
    LoginComponent,
    EmptyComponent,
    TestDialogComponent,
    LoginComponent,
    ProfileComponent,
    RegisterComponent,
    EditRemoveCourseComponent,
    AreYouSureComponent
  ],
  entryComponents: [
    HomeComponent, TestDialogComponent, LoginComponent
  ],
  providers: [HttpClientModule, {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
