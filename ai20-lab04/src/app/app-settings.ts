import {HttpHeaders} from '@angular/common/http';
import {HateoasModel} from './models/hateoas.model';

export class AppSettings {
  public static RETRIES = 0;
  public static JSON_HTTP_OPTIONS: object = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8',
      Accept: 'application/json' // , text/plain, */*
    })
    // , responseType: 'json'
  };
}

// Dynamic build of r1 tabs menu
export const tabs = [
  {path: 'students', label: 'Students'},
  {path: 'vms', label: 'VMs'},
  {path: 'groups', label: 'Groups'},
  {path: 'assignments', label: 'Assignments'}
];

export function removeHATEOAS(i: HateoasModel): [] {
  let x: any = i?._embedded;
  if (x?.courseDTOList != null) {
    x = x.courseDTOList;
  }
  if (x?.studentDTOList != null) {
    x = x.studentDTOList;
  }
  const courses: any = x?.map((course: any) => {
    delete course?._links;
    delete course?.links;
    return course;
  });
  delete courses?._links;
  delete courses?.links; // only '_links' should show up
  return courses;
}
