import { Injectable } from '@angular/core';
import { Observable, throwError, interval } from 'rxjs';
import { catchError, switchMap, startWith, map } from 'rxjs/operators';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { User, ArchiveUser } from '../models/user.model';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class AdminApiService {

  // MOCK json source folder path
  MOCK_JSON_SRC_PATH = './assets/JsonFiles/admin/';
  ownerId: number;
  appProperties;

  constructor(
    private _httpClient: HttpClient,
    private _sharedSvc: SharedService
  ) {}

  setProperties() {
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    console.log('==========================');
    console.log(this.appProperties, 'this.appProperties==========================');
    console.log('==========================');
  }

/**
* getPermissions fetches
*/
public getPermissionsList(): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.secApi.module}/json/users/permissions`, { withCredentials: true}).pipe(catchError(this.handleError));
}

/**
* gerUserList fetches
*/
public getUserList(ownerId, isIntervalEnable): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this.getByInterval(this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}entities/entityId/PASUSER/ownerId/${ownerId}?orderBy=id&isAscending=false`, { withCredentials: true}).pipe(catchError(this.handleError)), isIntervalEnable);
}

/**
* getUserRoleList() fetches the User Role list
*/
public getUserRoleList(): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}companies/domainName/${this.appProperties.admin_ui.DOMAIN}/roles`, { withCredentials: true}).pipe(catchError(this.handleError));
}

/**
* getUserRoleList() fetches the User Role list
*/
public getUserRoleDetails(userName: String): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/roles`, { withCredentials: true}).pipe(catchError(this.handleError));
}

/**
* updateUserRoles() fetches the User Role list
*/
public updateUserRoles(userName: String, userRoles: any): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.put(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/userName/${userName}/roles?domainName=${this.appProperties.admin_ui.DOMAIN}`, userRoles, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

/**
* getUser() fetches the User detail based on User Login Name
*/
public getUser(id: number): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}entities/entityId/${this.appProperties.admin_ui.ENTITY_ID}/id/${id}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public getUserId(userName: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/id`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public getUserProfile(id: number): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}entities/entityId/PASUSER/id/${id}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public getUserForPwdReset(encryptedCode: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/passwordReset?encryptedCode=${encodeURIComponent(encryptedCode)}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public getUserIdForPwdReset(userName: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/domainName/${this.appProperties.admin_ui.DOMAIN}/userName/${userName}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public getOwnerId(domainName: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}companies/domainName/${domainName}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public validatePasswordAtAPILevel(userName: string, password: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/domainName/${this.appProperties.DOMAIN}/userName/${userName}/password/${password}/validate`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public checkEmailDuplication(emailId: string): Observable<any> {
  const encodedEmail = encodeURIComponent(`email='${emailId}'`);
  console.log(encodedEmail, 'encodedEmail');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users?filterExpression=${encodedEmail}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

public checkUserNameDuplication(username): Observable<any> {
  const encodedUsername = encodeURIComponent(`loginName='${username}'`);
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users?filterExpression=${encodedUsername}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

/**
 * addUser inserts a new user
*/
public addUser(user: User, queryString: string): Observable<any> {
  const header = new HttpHeaders();
  header.append('Content-Type', 'application/json');
  // header.append("Access-Control-Allow-Credentials", "true");
  // header.append("Access-Control-Allow-Headers", "content-type, if-none-match");
  // header.append("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
  // header.append("Access-Control-Allow-Origin", "*");
  // header.append("Access-Control-Max-Age", "3600");
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users?${queryString}`, user, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
  // tslint:disable-next-line:max-line-length
  // return this._httpClient.post(`http://www.test-roche.com:92/admin-api/json/users?ownerId=1&roles=SystemUser`, user, {headers: header,withCredentials: true}).pipe(catchError(this.handleError));
}

/**
 * updateUser() updates an user
*/
public updateUser(user: User, queryString: string): Observable<any> {
  const header = new HttpHeaders();
  header.append('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.put(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users?${queryString}`, user, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

/**
* archiveUser makes an active user to archive and vice versa.
*/
public updateUserStatus(user: ArchiveUser): Observable<any> {
  // return this._httpClient.get(`${this.MOCK_JSON_SRC_PATH}/user-list.json`).pipe(catchError(this.handleError));
  const header = new HttpHeaders();
  header.append('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.put(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users?ownerId=1`, user, { withCredentials: true}).pipe(catchError(this.handleError));
}

/**
* archiveUser makes an active user to archive and vice versa.
*/
public unArchiveUser(userName: string): Observable<any> {
  // return this._httpClient.get(`${this.MOCK_JSON_SRC_PATH}/user-list.json`).pipe(catchError(this.handleError));
  // const header = new HttpHeaders();
  // header.append('Content-Type', 'application/json');
  // // tslint:disable-next-line:max-line-length
  // tslint:disable-next-line:max-line-length
  // return this._httpClient.put(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users?ownerId=1`, user, { withCredentials: true}).pipe(catchError(this.handleError));

  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/activate`, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
}

/**
* updateUserStatusIncDelete makes an active user to archive and vice versa.
* Added for defect RC-1869.
*/
public updateUserStatusIncDelete(user: ArchiveUser): Observable<any> {
  // return this._httpClient.get(`${this.MOCK_JSON_SRC_PATH}/user-list.json`).pipe(catchError(this.handleError));
  const header = new HttpHeaders();
  header.append('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.delete(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/userName/${user.loginName}`, { withCredentials: true}).pipe(catchError(this.handleError));
}

/**
 * To archive an user
 * Included in PAS 4.3 upgradation
 * @param userName
 */
public archiveUser(userName: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.delete(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/deactivate`, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
}

public resetPwd(pwd: any) {
  const header = new HttpHeaders();
  header.append('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.put(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/password`, pwd, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

public disableEncryptionCode(data: any) {
  const header = new HttpHeaders();
  header.append('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.put(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}users/passwordReset`, data, {headers:  new HttpHeaders({ 'Content-Type':  'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

public resetPwdWithoutAuth(encodedValue) {
  // tslint:disable-next-line:max-line-length

  const body = new HttpParams()
      .set('token', encodedValue.token)
      .set('password', encodedValue.password);

    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admApi.port}/${this.appProperties.admApi.module}/anonymous/json/users/password`, body.toString(), { headers: headers } ).pipe(catchError(this.handleError));
}

public validateUser(credentials) {
  // tslint:disable-next-line:max-line-length

  const body = new HttpParams()
      .set('userName', credentials.userName)
      .set('domainName', this.appProperties.admin_ui.DOMAIN )
      .set('newPassword', credentials.newPassword)
      .set('oldPassword', credentials.oldPassword);

      const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.secApi.module}/json/users/password/change`, body.toString(), { headers: headers } ).pipe(catchError(this.handleError));

  // tslint:disable-next-line:max-line-length
  // return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.secApi.module}/json/users/userName/${credentials.userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/newPassword/${newpwd}?oldPassword=${credentials.password}`, {}, {headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

public checkWithOldPassword(userName: string, password: any) {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.secApi.module}/json/users/domainName/${this.appProperties.admin_ui.DOMAIN}/userName/${userName}/password/${password}/validate`, { withCredentials: true} ).pipe(catchError(this.handleError));
}

public changePwdExistUser(userName: string, password: any) {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.secApi.module}/json/users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/newPassword/${password}`, {}, {headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

public checkUserNameExist(userName: string) {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.secApi.module}/anonymous/json/users/domainName/${this.appProperties.admin_ui.DOMAIN}/userName/${userName}`, {withCredentials: true} ).pipe(catchError(this.handleError));
}

public saveResetEmailDetails(userName: string, domainName: string, emailId: string, encodedUrl: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/domain/${domainName}/reset/user/${userName}/password/email/${encodeURIComponent(emailId)}/URL/${encodedUrl}`, {}, {headers:  new HttpHeaders({ 'Content-Type':  'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

public sendForgotEmailDetails(userName: string, domainName: string, emailId: string, encodedUrl: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admApi.port}/${this.appProperties.admApi.module}/anonymous/json/users/domain/${domainName}/forgot/user/${userName}/password/email/${encodeURIComponent(emailId)}/URL/${encodedUrl}`, {}, {headers:  new HttpHeaders({ 'Content-Type':  'application/json' }), withCredentials: true} ).pipe(catchError(this.handleError));
}

public getNotifications() {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admmApi.port}/${this.appProperties.admmApi.module}/json/rest/api/v1/notification`, {withCredentials: true} ).pipe(catchError(this.handleError));
  // tslint:disable-next-line:max-line-length
  // 'getNotification': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.admmApi.port}/${this.apiProp.admmApi.module}/json/rest/api/v1/notification`,
}


/**
 * Storing the image file in DB
 * @labLogo image file
 */
public addLabLogo(labLogo) {
  console.log('labLogo', labLogo);
  const headers = new HttpHeaders().set('Content-Type', 'multipart/form-data');
  const {protocol, host, admmApi  } = this.appProperties;
  const XHRAPI = `${protocol}://${host}:${admmApi.port}/${admmApi.module}/upload`;
  return this._httpClient.post(XHRAPI, labLogo, {withCredentials: true, responseType: 'text'} ).pipe(catchError(this.handleError));
}

/**
 * fetching the lab settings information by calling XHR call
 */
public getLabSettings() {
  const {protocol, host, admmApi  } = this.appProperties;
  const XHRAPI = `${protocol}://${host}:${admmApi.port}/${admmApi.module}/json/rest/api/v1/systemsettings`;
  return this._httpClient.get(`${XHRAPI}`, { withCredentials: true }).pipe(catchError(this.handleError));
}

/**
 * Storing lab settings Info by passing the lab settings data
 * @labSettingsDatalab setting info
 */
public saveUpdateLabSettings(labSettingsData) {
  const headers = new HttpHeaders().set('Content-Type', 'application/json');
  const {protocol, host, admmApi  } = this.appProperties;
  const XHRAPI = `${protocol}://${host}:${admmApi.port}/${admmApi.module}/json/rest/api/v1/systemsettings`;
  return this._httpClient.post(XHRAPI, labSettingsData, {headers: headers, withCredentials: true, responseType: 'text'} )
  .pipe(catchError(this.handleError));
}

/**
 * fetching the lab logo type of blob
 */
public getLabLogo() {
  const {protocol, host, admmApi  } = this.appProperties;
  const XHRAPI = `${protocol}://${host}:${admmApi.port}/${admmApi.module}/json/rest/api/v1/lablogo`;
  return this._httpClient.get(XHRAPI,   { withCredentials: true, responseType: 'blob'}).pipe(catchError(this.handleError));
}

public labInfoPreview(formData) {
  // const headers = new HttpHeaders().set('Content-Type', 'multipart/form-data');
  const {protocol, host, admmApi  } = this.appProperties;
  const XHRAPI = `${protocol}://${host}:${admmApi.port}/${admmApi.module}/labdetailpreviewreport`;
  return this._httpClient.post(XHRAPI, formData, { withCredentials: true,  responseType: 'blob'}).pipe(catchError(this.parseErrorBlob));
}

public checkAuth(credentials) {
  const apiProp = this.appProperties;
  const body = new HttpParams()
    .set('j_username', credentials.userName)
    .set('j_password', credentials.password)
    .set('org', apiProp.asmin_ui.DOMAIN);

  const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${apiProp.protocol}://${apiProp.host}:${apiProp.secApi.port}/${apiProp.secApi.module}/json/security/login`,
  body.toString(), { headers: headers, responseType: 'text' }).pipe(catchError(this.handleError));
}

public logout(token: any): Observable<any> {
  const data = new HttpParams().set('token', token);
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.secApi.port}/${this.appProperties.admin_ui.SECURITY_API}security/logout`, data, {headers:  new HttpHeaders({ 'Content-Type':  'application/x-www-form-urlencoded' }), withCredentials: true} ).pipe(catchError(this.handleError));
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

parseErrorBlob(err: HttpErrorResponse): Observable<any> {
  const reader: FileReader = new FileReader();

  const obs = Observable.create((observer: any) => {
    reader.onloadend = (e) => {
      observer.error(reader.result);
      observer.complete();
    };
  });
  reader.readAsText(err.error);
  return obs;
}

getByInterval(apiObser, isIntervalEnable) {
  const intervalTime = this.appProperties ? this.appProperties.apiIntervalTime : null;
  if (isIntervalEnable && intervalTime !== undefined && intervalTime !== null && intervalTime !== '') {
    const _interval = interval(intervalTime);
    return _interval
    .pipe(
      startWith(0),
      switchMap(() => apiObser)
    );
  } else {
    return apiObser;
  }
}



// checkAuth(): Observable<any>{
//   const body = new HttpParams()
//   .set('j_username', 'admin')
//   .set('j_password', 'pas123')
//   .set('org', 'hcl.com');

//   let headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
//   return this._httpClient.post(`/security-web/json/security/login`, body.toString(), {headers: headers, responseType:'text'});
// }
}
