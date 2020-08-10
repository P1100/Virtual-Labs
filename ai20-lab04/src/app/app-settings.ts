import {HttpHeaders} from '@angular/common/http';

export class AppSettings {
  public static RETRIES = 2;
  // public static JSON_HTTP_OPTIONS = {headers: new HttpHeaders({'Content-Type': 'application/json'}), responseType: 'json'};
  public static JSON_HTTP_OPTIONS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8',
      Accept: 'application/json, text/plain'
    })
  };
}
