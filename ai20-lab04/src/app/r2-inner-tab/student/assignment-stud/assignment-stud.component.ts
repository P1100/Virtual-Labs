import {Component, ElementRef, Input, ViewChild} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Implementation} from '../../../models/implementation.model';
import {MatTableDataSource} from '@angular/material/table';
import {Assignment} from '../../../models/assignment.model';
import {MatSort} from '@angular/material/sort';
import {AlertsService} from '../../../services/alerts.service';
import {Image} from '../../../models/image.model';
import {ImageService} from '../../../services/image.service';
import {VlServiceService} from '../../../services/vl-service.service';

@Component({
  selector: 'app-assignment-stud',
  templateUrl: './assignment-stud.component.html',
  styleUrls: ['./assignment-stud.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AssignmentStudComponent {
  columnsToDisplay: string[] = ['nav', 'name', 'releaseDate', 'link'];
  columnsToDisplayImplementation: string[] = ['firstName', 'lastName', 'id', 'status', 'timestamp', 'submit'];
  expandedElement: Implementation | null;
  // statusArray = ['NULL', 'READ', 'SUBMITTED', 'REVIEWED', 'DEFINITIVE'];
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  dialogRef: MatDialogRef<any>;
  dataSourceAssignments = new MatTableDataSource<Assignment>();
  idLoggedStudent: number;

  selectedImageFile: File;
  uploadImageData: FormData;
  @ViewChild('labelImageFile')
  label: ElementRef;

  @Input()
  set assignments(array: Assignment[]) {
    if (array == null) {
      this.dataSourceAssignments.data = [];
      return;
    }
    this.dataSourceAssignments.data = [...array];
    // Should help making sure table data is loaded when sort is assigned
    setTimeout(() => {
      this.dataSourceAssignments.sort = this.sort;
    });
  }
  get assignments(): Assignment[] {
    return this.dataSourceAssignments.data;
  }
  constructor(private alertsService: AlertsService, public dialog: MatDialog, public imageService: ImageService, public vlServiceService: VlServiceService) {
    this.idLoggedStudent = +localStorage.getItem('id');
  }

  public onFileChanged(event) {
    this.selectedImageFile = event.target.files[0];
    // To update bootstrap input text, without JQuery
    this.label.nativeElement.innerText = this.selectedImageFile?.name;
  }
  onSubmit(impl: Implementation) {
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
          },
          e => {
            this.alertsService.setAlert('danger', 'Upload image error in registration. ' + e);
            this.dialogRef.close();
            return;
          });
      });
    } else {
      promise = new Promise((resolve, reject) => {
        resolve(1);
      });
    }
    promise.then(() => {
      const imageId = +returnedImageDto?.id;
      this.vlServiceService.uploadSubmission(impl.id, imageId).subscribe(
        () => {
          this.alertsService.setAlert('success', 'File submitted!');
        },
        e => {
          this.alertsService.setAlert('danger', 'Couldn\'t upload submission! ' + e);
        }
      );
    });
  }

  setStatusToRead(assignmentId: number) {
    this.vlServiceService.setStatusSubmissionToRead(assignmentId, this.idLoggedStudent).subscribe(
      () => {
        this.alertsService.setAlert('success', 'Status updated!');
      },
      e => {
        this.alertsService.setAlert('danger', 'Couldn\'t update status ' + e);
      }
    );
  }
}
