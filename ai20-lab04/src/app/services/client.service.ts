import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'}), // ,  'Access-Control-Allow-Origin': '*'
// observe?: 'body' | 'events' | 'response',
//   params?: HttpParams|{[param: string]: string | string[]},
//     reportProgress: true,
    // responseType: 'json'|'text',
    // withCredentials: true  // Whether this request should be sent with outgoing credentials (cookies).
  };
  private apiJsonServerProxyPath = environment.HttpOrHttpsPrefix + '://localhost:8080/API'; // URL to web api

  constructor(private http: HttpClient) {
    // console.log('@@ BackendService.constructor - new service instance (http or htpps?)=' + environment.HttpOrHttpsPrefix);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }
}
