import {Component, ElementRef, ViewChild} from '@angular/core';
import {AppSettings} from '../../app-settings';
import {CourseService} from '../../services/course.service';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {User} from '../../models/user.model';
import {AuthService} from '../../services/auth.service';
import {Observable} from 'rxjs';
import {ImageService} from '../../services/image.service';
import {Image} from '../../models/image.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styles: ['mat-form-field {width: 100%} mat-radio-button {margin-right: 12px}']
})
export class RegisterComponent {
  user = new User(null, null, null, null, null,  'd000000@polito.it', []);
  checkboxNoValidate = true;
  showCheckboxNoValidateForTesting = AppSettings.devtest;
  isStudentRadio = 'student';
  selectedImageFile: File;
  uploadImageData: FormData;
  @ViewChild('labelImageFile')
  label: ElementRef;

  constructor(private courseService: CourseService, public dialogRef: MatDialogRef<RegisterComponent>, private router: Router,
              private alertsService: AlertsService, private authService: AuthService, public imageService: ImageService) {
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    let obs: Observable<any>;
    let returnedImageDto: Image = null;
    let promise: Promise<any>;
    // Profile Image Upload
    if (this.selectedImageFile != null) {
      this.uploadImageData = new FormData();
      // 'imageFile' is the param value used by the Spring api
      this.uploadImageData.append('imageFile', this.selectedImageFile, this.selectedImageFile.name);
      // obs = forkJoin([obs, this.imageService.uploadImage(this.uploadImageData)]);
      promise = new Promise((resolve, reject) => {
        this.imageService.uploadImage(this.uploadImageData).subscribe(x => {
          returnedImageDto = x;
          resolve(returnedImageDto);
        });
      });
    } else {
      promise = new Promise((resolve, reject) => {
        resolve(1);
      });
    }
    promise.then(() => {
      this.user.imageId = +returnedImageDto?.id;
      if (this.isStudentRadio == 'student') {
        obs = this.authService.registerStudent(this.user);
      } else {
        obs = this.authService.registerProfessor(this.user);
      }
      obs.subscribe(
        () => {
          // Check to show test (dev) image icon on home (dialog return value != 0)
          if (returnedImageDto != null) {
            this.dialogRef.close(returnedImageDto.id);
          } else {
            this.dialogRef.close(0);
          }
          this.router.navigateByUrl('/');
          this.alertsService.setAlert('success', 'User registered. Check email for confirmation');
        },
        e => {
          this.dialogRef.close();
          this.alertsService.setAlert('danger', 'Couldn\'t register user! ' + e);
        }
      );
    });
  }
  public onFileChanged(event) {
    this.selectedImageFile = event.target.files[0];
    // To update bootstrap input text, without JQuery
    this.label.nativeElement.innerText = this.selectedImageFile?.name;
  }
}
