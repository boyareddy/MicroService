import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { HttpParams, HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { SharedService } from './shared.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SharedAuthService {

  constructor(private _http: HttpClient, private _sharedService: SharedService) { }
  /**
   * isAuthenticated checks the authStatus of an user and returns a boolean.
   */
  isAuthenticated() {
    // authStatus is 'true' if user has valid session else it is false.
    const authStatus = sessionStorage.getItem('authStatus');

    if (authStatus) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * setAuthStatus stores a value 'true' in browser local storage with the key 'authStatus'.
   */
  setAuthStatus() {
    sessionStorage.setItem('authStatus', 'true');
  }

  /**
   * deleteAuthStatus deletes the key 'authStatus' from browser local storage.
   */
  deleteAuthStatus() {
    sessionStorage.removeItem('authStatus');
  }

  public logout(token: any): Observable<any> {
    const apiProp = this._sharedService.getData('appProperties');
    const data = new HttpParams().set('token', token);
    // tslint:disable-next-line:max-line-length
    return this._http.post(`${apiProp.protocol}://${apiProp.host}:${apiProp.secApi.port}/${apiProp.secApi.module}/json/security/logout`, data, {headers:  new HttpHeaders({ 'Content-Type':  'application/x-www-form-urlencoded' }), withCredentials: true} ).pipe(catchError(this.handleError));
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
