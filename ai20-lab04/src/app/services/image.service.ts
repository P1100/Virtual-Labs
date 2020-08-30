import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, retry, tap} from 'rxjs/operators';
import {AppSettings, formatErrors} from '../app-settings';
import {Image} from '../models/image.model';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private baseUrlAPI = AppSettings.baseUrl + '/images';

  constructor(private http: HttpClient) {
  }

  uploadImage(uploadImageData: FormData): Observable<HttpResponse<any>> {
    return this.http.post<FormData>(`${this.baseUrlAPI}/API/images`, uploadImageData, {observe: 'response'})
      .pipe(
        catchError(formatErrors),
        tap(res => console.log('--uploadImage:', res))
      );
  }
  getImage(idImage: string): Observable<Image> {
    // Make a call to Sprinf Boot to get the Image Bytes.
    return this.http.get<Image>(`${this.baseUrlAPI}/get/${idImage}`)
      .pipe(
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getImage:', res))
      );
  }
}
