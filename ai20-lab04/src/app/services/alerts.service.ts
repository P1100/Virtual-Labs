import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

export interface Alert {
  type: string; // Can be: 'success','info','warning','danger','primary','secondary','light','dark'
  message: string;
}

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
}
