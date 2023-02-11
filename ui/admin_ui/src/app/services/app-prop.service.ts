import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AppPropService {

  constructor(private _http: HttpClient) { }

  getAppProperties() {
    return this._http.get('/ui-conf/api-properties.json').pipe(catchError(this.handleError));
  }

  getFlagsFromAmm(deviceType: string, apiProp: any): Observable<any> {
    return this._http.get(`${apiProp.protocol}://${apiProp.host}:${apiProp.ammApi.port}/${apiProp.ammApi.module}/json/rest/api/v1/assay/flagDescriptions/${deviceType}`).pipe(catchError(this.handleError));
  }

  public handleError(err: HttpErrorResponse) {
    let errMsg = '';
    if (err.error instanceof Error) {
      errMsg = err.error.message;
    } else {
      errMsg = err.error;
    }
    return throwError(errMsg);
  }
}
