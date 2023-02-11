import { Injectable } from '@angular/core';
import { SharedService } from '../shared/shared.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RsrService {

  constructor(
    private _sharedService: SharedService,
    private _http: HttpClient) { }

  /**
   * RSRLogin validate the token by posting token and id.
   * @param loginInfo RSR form Info
   */
  rsrLogin(loginInfo) {
    const apiProp = this._sharedService.getData('appProperties');
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const url = `${apiProp.protocol}://${apiProp.host}:${apiProp.admmApi.port}/${apiProp.admmApi.module}/json/rest/api/v1/validatersrtoken`;
    return this._http.post(`${url}`, loginInfo, {headers: headers});
  }
}
