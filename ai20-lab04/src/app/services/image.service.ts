import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, retry, tap} from 'rxjs/operators';
import {AppSettings, formatErrors} from '../app-settings';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private baseUrlAPI = environment.baseUrl + '/images';

  constructor(private http: HttpClient) {
  }

  uploadImage(uploadImageData: FormData): Observable<HttpResponse<any>> {
    // Make a call to the Spring Boot Application to save the image
    return this.http.post<FormData>(`${this.baseUrlAPI}/upload`, uploadImageData, {observe: 'response'})
      .pipe(
        catchError(formatErrors),
        tap(res => console.log('--uploadImage:', res))
      );
  }
  getImage(idImage: string) {
    // Make a call to Sprinf Boot to get the Image Bytes.
    return this.http.get(`${this.baseUrlAPI}/get/${idImage}`)
      .pipe(
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getImage:', res))
      );
  }
}
