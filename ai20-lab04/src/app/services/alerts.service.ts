import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

export interface Alert {
  type: string; // Can be: 'success','info','warning','danger','primary','secondary','light','dark'
  message: string;
}

const httpCodes = {
  100: '100 Continue',
  101: '101 Switching Protocols',
  200: '200 OK',
  201: '201 Created',
  202: '202 Accepted',
  203: '203 Non-Authoritative Information',
  204: '204 No Content',
  205: '205 Reset Content',
  206: '206 Partial Content',
  300: '300 Multiple Choices',
  301: '301 Moved Permanently',
  302: '302 Found',
  303: '303 See Other',
  304: '304 Not Modified',
  305: '305 Use Proxy',
  307: '307 Temporary Redirect',
  400: '400 Bad Request',
  401: '401 Unauthorized',
  402: '402 Payment Required',
  403: '403 Forbidden',
  404: '404 Not Found',
  405: '405 Method Not Allowed',
  406: '406 Not Acceptable',
  407: '407 Proxy Authentication Required',
  408: '408 Request Time-out',
  409: '409 Conflict',
  410: '410 Gone',
  411: '411 Length Required',
  412: '412 Precondition Failed',
  413: '413 Request Entity Too Large',
  414: '414 Request-URI Too Large',
  415: '415 Unsupported Media Type',
  416: '416 Requested Range Not Satisfiable',
  417: '417 Expectation Failed',
  422: '422 Unprocessable Entity',
  500: '500 Internal Server Error',
  501: '501 Not Implemented',
  502: '502 Bad Gateway',
  503: '503 Service Unavailable',
  504: '504 Gateway Time-out',
  505: '505 HTTP Version Not Supported',
};

@Injectable({
  providedIn: 'root'
})
export class AlertsService {
  private alertSubject: BehaviorSubject<Alert> = new BehaviorSubject(null);

  public getAlertSubject(): Observable<Alert> {
    return this.alertSubject as Observable<Alert>;
  }
  // {type: 'success', message: 'Course added!'}
  public setAlert(atype: string, amessage: string) {
    const alert: Alert = {type: atype, message: `${amessage}`};
    this.alertSubject.next(alert);
  }
  public closeAlert() {
    this.alertSubject.next(null);
  }

  public static getHttpResponseStatusDescription(code: number): string {
    return httpCodes[code];
  }
}
