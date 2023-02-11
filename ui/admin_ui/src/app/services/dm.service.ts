import { Injectable } from '@angular/core';
import { SharedService } from './shared.service';
import { Observable, throwError, interval } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, startWith, switchMap } from 'rxjs/operators';
import { DeviceForm, Auth, UserDetails } from '../models/device.model';

@Injectable({
  providedIn: 'root'
})
export class DmService {

  appProperties: any;
  dmApiEnd: string;
  rmmApiEnd: string;
  dmDefApiEnd: string;
  uiConf: string;
  admAE: string;

  constructor(private _httpClient: HttpClient,
              private _sharedSvc: SharedService) {
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    // tslint:disable-next-line:max-line-length
    this.dmApiEnd = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.dmmApi.port}/${this.appProperties.dmmApi.module}/json`;
    // tslint:disable-next-line:max-line-length
    this.dmDefApiEnd = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.dmmApi.port}/${this.appProperties.dmmApi.module}`;
    // tslint:disable-next-line:max-line-length
    this.rmmApiEnd = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.rmmApi.port}/${this.appProperties.rmmApi.module}/json`;
    // tslint:disable-next-line:max-line-length
    this.uiConf = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.appPort}/admin_ui/assets/JsonFiles/dmm/`;
    this.admAE = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admmApi.port}/${this.appProperties.admmApi.module}/`;
  }

  /**
  * To fetch list of devices.
  */
  public getDeviceList(isIntervalEnable): Observable<any> {
    // tslint:disable-next-line:max-line-length
    return this.getByInterval(this._httpClient.get(`${this.dmApiEnd}/device/fetch`, { withCredentials: true}).pipe(catchError(this.handleError)), isIntervalEnable);
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/listdevice.json').pipe(catchError(this.handleError));
  }

  /**
  * To fetch list of device types.
  */
  public getDeviceTypes(): Observable<any> {
    return this._httpClient.get(`${this.dmApiEnd}/devicetype/fetch`, { withCredentials: true}).pipe(catchError(this.handleError));
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/listdevice.json').pipe(catchError(this.handleError));
  }

  public getDeviceStatuses(): Observable<any> {
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/device-statues.json').pipe(catchError(this.handleError));
    return this._httpClient.get(`${this.uiConf}device-statues.json`).pipe(catchError(this.handleError));
  }

  public getProtocols(): Observable<any> {
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/device-protocols.json').pipe(catchError(this.handleError));
    return this._httpClient.get(`${this.uiConf}device-protocols.json`).pipe(catchError(this.handleError));
  }

  /**
   * To get list of unregistered devices.
  */
  public getUnregisteredDevices(): Observable<any> {
      // tslint:disable-next-line:max-line-length
      return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/unregistered-devices.json').pipe(catchError(this.handleError));
  }

  public getDeviceDetail(deviceSerialNo): Observable<any> {
      const encodedSerialNo: string = encodeURIComponent(`serialNo='${deviceSerialNo}'`);
      // tslint:disable-next-line:max-line-length
      return this._httpClient.get(`${this.dmApiEnd}/device/fetch/expr?filterExpression=${encodedSerialNo}`, { withCredentials: true}).pipe(catchError(this.handleError));
      // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/device-detail.json').pipe(catchError(this.handleError));
  }

  public getDevRunStatus(deviceId: string, runStatus: string): Observable<any> {
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/device-protocols.json').pipe(catchError(this.handleError));
    // tslint:disable-next-line:max-line-length
    return this._httpClient.get(`${this.rmmApiEnd}/rest/api/v1/runresults/sampleresultslist?deviceid=${deviceId}&runstatus=${runStatus}`).pipe(catchError(this.handleError));
  }

  public getDeviceIcon(devSlNo: string, devPrId: string, iconExt: string, newDevice: any): string {
    // return this._httpClient.post(`${this.uiConf}unregistered-devices.json`).pipe(catchError(this.handleError));
    return `${this.dmDefApiEnd}/file/files/${devPrId}/${devSlNo}.${iconExt}`;
  }

  public validateOutputDir(outputDir: string, deviceTypeVal: string): Observable<any> {
    const encodedOutputFileDir: string = encodeURIComponent(outputDir);
    const deviceType: string = encodeURIComponent(deviceTypeVal);
    // tslint:disable-next-line:max-line-length
    return this._httpClient.get(`${this.admAE}json/rest/api/v1/validatePath?deviceType=${deviceType}&outputPath=${encodedOutputFileDir}`, { withCredentials: true,  responseType: 'text' }).pipe(catchError(this.handleErrorObject));
  }

  public validateOutputDirForMultipleDevice(outputDir: string, deviceTypeVal: string): Observable<any> {
    const encodedOutputFileDir: string = encodeURIComponent(outputDir);
    const deviceType: string = encodeURIComponent(deviceTypeVal);
    // tslint:disable-next-line:max-line-length
    return this._httpClient.get(`${this.admAE}json/rest/api/v1/validatePath?outputPath=${encodedOutputFileDir}&deviceType=${deviceType}`, { withCredentials: true,  responseType: 'text' }).pipe(catchError(this.handleErrorObject));
  }

  /**
    * To insert new unregistered device.
  */
  public postUnRegDevice(deviceId: string, unRegDevice: any): Observable<any> {
    // tslint:disable-next-line:max-line-length
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/unregistered-devices.json').pipe(catchError(this.handleError));
    // tslint:disable-next-line:max-line-length
    return this._httpClient.post(`${this.dmApiEnd}/device/updateAttribute/${deviceId}`, unRegDevice, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
  }

  /**
    * To insert new unregistered device.
  */
  public postNewRegDevice(newDevice: any): Observable<any> {
    // tslint:disable-next-line:max-line-length
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/unregistered-devices.json').pipe(catchError(this.handleError));
    // tslint:disable-next-line:max-line-length
    return this._httpClient.post(`${this.dmApiEnd}/device/add`, newDevice, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
  }

  /**
    * Map the device id and user details
  */
 public mapDeviceAndUser(deviceId: string, userName: string): Observable<any> {
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.dmApiEnd}/device/update/deviceuser/${deviceId}/username/${userName}`, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
}

  public postDeviceDetail(deviceType: string, deviceToUpdate: any): Observable<any> {
    // tslint:disable-next-line:max-line-length
    return this._httpClient.post(`${this.dmApiEnd}/device/update/${deviceType}`, deviceToUpdate, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
    // return this._httpClient.get('http://localhost:4200/assets/JsonFiles/dmm/update-device.json').pipe(catchError(this.handleError));
  }

  public postDeviceIcon(devSlNo: string, devPrId: string, iconExt: string, newDevice: any): Observable<any> {
    // return this._httpClient.post(`${this.uiConf}unregistered-devices.json`).pipe(catchError(this.handleError));
    const formData: FormData = new FormData();
    formData.append('file', newDevice, newDevice.name);
    // tslint:disable-next-line:max-line-length
    return this._httpClient.post(`${this.dmDefApiEnd}/file/upload?name=${devSlNo}&parent-UUID=${devPrId}&file-extension=${iconExt}`, formData, {withCredentials: true}).pipe(catchError(this.handleError));
  }

  public deleteDevice(deviceId: string): Observable<any> {
    // tslint:disable-next-line:max-line-length
    return this._httpClient.delete(`${this.dmApiEnd}/device/delete/${deviceId}`, { withCredentials: true}).pipe(catchError(this.handleError));
  }

  public deleteDeviceIcon(devSlNo: string, devPrId: string, iconExt: string, newDevice: any): Observable<any> {
    // return this._httpClient.post(`${this.uiConf}unregistered-devices.json`).pipe(catchError(this.handleError));
    // tslint:disable-next-line:max-line-length
    return this._httpClient.delete(`${this.dmDefApiEnd}/file/delete?name=${devSlNo}&parent-UUID=${devPrId}&file-extension=${iconExt}`, {withCredentials: true}).pipe(catchError(this.handleError));
  }

  public checkDuplicateDevice(data: string) {
    // tslint:disable-next-line:max-line-length
    return this._httpClient.get(`${this.dmApiEnd}/device/fetch/expr?filterExpression=${data}`, {withCredentials: true}).pipe(catchError(this.handleError));
  }

  public exportReport(locale: string): Observable<any>{
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf');
    return this._httpClient.get(`${this.appProperties.protocol}://${this.appProperties.host}:86/rmm/json/rest/api/v1/jasper/${locale}`, { headers: headers, responseType: 'blob' }).pipe(catchError(this.handleError));
  }

  public addDeviceAuth(device: DeviceForm): Observable<any>{
    let userDetails: UserDetails = this.appProperties.devAuth;
    userDetails.loginName = device.clientId;
    userDetails.password = btoa(device.password);
    return this._httpClient.post(`${this.admAE}json/rest/api/v1/adddeviceuser`, userDetails, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true, responseType: 'text'}).pipe(catchError(this.handleError));
  }

  deactivateDevice(userName) {
    // tslint:disable-next-line:max-line-length
    return this._httpClient.delete(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/deactivate`, {headers:  new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
  }

  activateDevice(userName) {
    // tslint:disable-next-line:max-line-length
    return this._httpClient.post(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admin_ui.API_PORT}/${this.appProperties.admin_ui.API_NAME}users/userName/${userName}/domainName/${this.appProperties.admin_ui.DOMAIN}/activate`, null).pipe(catchError(this.handleError));
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

  public handleErrorObject(err: HttpErrorResponse) {
    return throwError(err);
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

}
