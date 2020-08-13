import {HttpHeaders} from '@angular/common/http';

export class AppSettings {
  public static RETRIES = 2;
  public static JSON_HTTP_OPTIONS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8',
      Accept: 'application/json' // , text/plain, */*
    })
  };
}

// Costruzione dinamica delle tabs parte da qui
export const tabs = [
  {path: 'students', label: 'Students'},
  {path: 'vms', label: 'VMs'},
  {path: 'groups', label: 'Groups'},
  {path: 'assignments', label: 'Assignments'}
];
