import {HttpHeaders} from '@angular/common/http';
import {HateoasModel} from './models/hateoas.model';
import {throwError} from 'rxjs';

export class AppSettings {
  public static RETRIES = 0;
  public static JSON_HTTP_OPTIONS: object = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8',
      Accept: 'application/json', // , text/plain, */*
    })
    , responseType: 'json'
  };
}

// Dynamic build of r1 tabs menu
export const tabs = [
  {path: 'students', label: 'Students'},
  {path: 'vms', label: 'VMs'},
  {path: 'teams', label: 'Teams'},
  {path: 'assignments', label: 'Assignments'}
];

/* HATEOAS API DESIGN DECISION: all http returned objects are converted to array (empty, full, or singleton), to uniform handling */
export function removeHATEOAS(container: HateoasModel): any[] {
  delete container?._links;
  delete container?.links;
  let innerList: any = container?._embedded;
  if (innerList?.courseDTOList != null) {
    innerList = innerList.courseDTOList;
  }
  if (innerList?.studentDTOList != null) {
    innerList = innerList.studentDTOList;
  }
  if (innerList?.professorDTOList != null) {
    innerList = innerList.professorDTOList;
  }
  if (innerList?.imageDTOList != null) {
    innerList = innerList.imageDTOList;
  }
  if (innerList?.teamDTOList != null) {
    innerList = innerList.teamDTOList;
  }
  if (innerList?.vmDTOList != null) {
    innerList = innerList.vmDTOList;
  }
  if (innerList?.implementationDTOList != null) {
    innerList = innerList.implementationDTOList;
  }
  innerList = innerList?.map((element: any) => {
    delete element?._links;
    delete element?.links;
    return element;
  });
  delete innerList?._links;
  delete innerList?.links; // only '_links' should show up

  if (innerList == null) {
    return Array.isArray(container) ? container : [container];
  }
  return Array.isArray(innerList) ? innerList : [innerList];
}

// Uniform all data received from services, to arrays (from objects)
export function getSafeDeepCopyArray(ss: any): any[] {
  return Array.isArray(ss) ? [...ss] : (ss != null ? [ss] : []);
}

export function formatErrors(error: any) {
  const s = error?.name + ': ' + error?.status + ' '
    + (error?.error?.error == null ? (typeof (error?.error) == 'string' ? error?.error : Object.keys(error?.error)) : error?.error?.error + ' - ' + error?.error?.message);
  console.error(s.replace(/undefined -/gi, ''));
  console.error(error);
  return throwError(error?.status);
}
