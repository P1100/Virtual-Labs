import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {AppSettings, formatErrors} from '../app-settings';
import {Image} from '../models/image.model';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private baseUrlApi = AppSettings.baseUrl + '/images';

  constructor(private http: HttpClient) {
  }

  uploadImage(uploadImageData: FormData): Observable<Image> {
    return this.http.post<Image>(`${this.baseUrlApi}`, uploadImageData, {observe: 'response'})
      .pipe(
        map(httpResponse => httpResponse.body),
        catchError(formatErrors),
        tap(res => console.log('--uploadImage:', res))
      );
  }
  getImage(idImage: number): Observable<Image> {
    // Make a call to Sprinf Boot to get the Image Bytes.
    return this.http.get<Image>(`${this.baseUrlApi}/${idImage}`)
      .pipe(
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getImage:', res))
      );
  }
}
