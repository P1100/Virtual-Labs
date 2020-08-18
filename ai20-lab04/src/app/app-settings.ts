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

export function removeHATEOAS(i: HateoasModel): any {
  delete i?._links;
  delete i?.links;
  let container: any = i?._embedded;
  if (container?.courseDTOList != null) {
    container = container.courseDTOList;
  }
  if (container?.studentDTOList != null) {
    container = container.studentDTOList;
  }
  const innerList: any = container?.map((element: any) => {
    delete element?._links;
    delete element?.links;
    return element;
  });
  delete innerList?._links;
  delete innerList?.links; // only '_links' should show up
  if (innerList == null) {
    return i;
  }
  return innerList;
}
