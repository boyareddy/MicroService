import { Injectable } from '@angular/core';
import { HttpHeaders, HttpParams, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoclizeServiceService {

  constructor(private _http: HttpClient) {

  }

  /**
    * supportedLangs is a getter method to fetches all the supported language codes.
  */
  get supportedLangs(): any {
      return this._http.get('assets/JsonFiles/loc/supported-langs.json').pipe(catchError(this.handleError));
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
