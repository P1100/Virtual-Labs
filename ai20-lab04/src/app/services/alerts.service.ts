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
  private alertMessage: Alert = null;  // null, or Alert
  private alertSubject: BehaviorSubject<Alert> = new BehaviorSubject(null);

  public getAlertSubject(): Observable<Alert> {
    return this.alertSubject as Observable<Alert>;
  }
  public setAlert(alert: Alert) {
    this.alertMessage = alert;
    this.alertSubject.next(alert);
  }
  public closeAlert() {
    this.alertMessage = null;
    this.alertSubject.next(null);
  }

}
