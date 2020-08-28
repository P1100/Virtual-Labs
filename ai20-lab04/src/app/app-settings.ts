import {HttpHeaders} from '@angular/common/http';
import {HateoasModel} from './models/hateoas.model';
import {throwError} from 'rxjs';
import {environment} from '../environments/environment';

/* Shared settings, constants and functions */
export class AppSettings {
  // Back end URL
  public static baseUrl = 'http://localhost:8080/API';
  public static devModeShowAll = environment.dev; // to show logs, tests, and other components

  // HTTP Settings (services)
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

/* HATEOAS API DESIGN: all http returned objects are converted to array (empty, full, or singleton), to uniform handling. TODO: look for alternatives, like @Projection */
export function removeHATEOAS(container: HateoasModel): any[] {
  if (container == null) {
    return [];
  }
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
  const result = innerList != null ? innerList : container;
  if (Array.isArray(result)) {
    return result;
  } else if (typeof result == 'object') {
    if (Object.keys(result)?.length == 0) {
      return [];
    } else {
      return [result];
    }
  } else {
    console.warn('HTTP HateOASModel: not an object, not an array.');
    return [];
  }
}

// Uniform all data received from services, converting any (mostly objects) to arrays
export function getSafeDeepCopyToArray(ss: any): any[] {
  return Array.isArray(ss) ? [...ss] : (ss != null ? [{...ss}] : []);
}
// TODO: test responseErrorString format
export function formatErrors(error: any) {
  let responseErrorString = error?.error?.message; // (`${(error?.error?.error == null ? (typeof (error?.error) == 'string' ? error?.error : Object.keys(error?.error)) : error?.error?.error + ' - ' + error?.error?.message)}`);
  if (AppSettings.devModeShowAll) {
    console.error('1-', error, '-----', error?.error, '-----', error?.error?.error, '-----', responseErrorString);
    responseErrorString = responseErrorString + ` [${error?.error?.status} ${error?.error?.error}]`;
  }
  return throwError(responseErrorString);
}
