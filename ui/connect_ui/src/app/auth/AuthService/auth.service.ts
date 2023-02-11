import { Injectable } from '@angular/core';
import { HttpHeaders, HttpParams, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SharedService } from '../../shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';

@Injectable()
export class AuthService {

  // MOCK JSON Configs
  mockDataUri = './assets/JsonFiles/';

  constructor(
    private _http: HttpClient,
    private _sharedService: SharedService,
    private _commonapiservice: CommonApiService
  ) { }

  checkAuth(credentials) {
    const apiProp = this._sharedService.getData('appProperties');
    const body = new HttpParams()
      .set('j_username', credentials.userName)
      .set('j_password', credentials.password)
      .set('org', apiProp.admin_ui.DOMAIN);

    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
    if (environment.production) {
      // tslint:disable-next-line:max-line-length
      return this._http.post(`${apiProp.protocol}://${apiProp.host}:${apiProp.secApi.port}/${apiProp.secApi.module}/json/security/login`,
      body.toString(), { headers: headers, responseType: 'text' }).pipe(catchError(this.handleError));
    } else {
      return this._http.get(`${this.mockDataUri}login.json`).pipe(catchError(this.handleError));
    }
  }

  getPermissions() {
    return this._commonapiservice.get('getPermissions');
  }

  /**
   * to get the userrole by passing userdetails to XHR
   * @param userDetails userdetails like username and domain
   */
  getUserRole(userDetails) {
    return this._commonapiservice.get('getUserRoleInfo', userDetails);
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
