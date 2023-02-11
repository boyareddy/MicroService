import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SimulatorService {

  constructor(private _http: HttpClient) { }
  private baseURL = 'http://localhost:3000/';
  headers = new HttpHeaders().set('Content-Type', 'application/json');

  addDevice(deviceInfo) {
    return this._http.post(`${this.baseURL}deviceDetails`, deviceInfo, {headers: this.headers}).pipe(catchError(this.handleError));
  }

  getDeviceList() {
   return this._http.get(`${this.baseURL}deviceDetails`).pipe(catchError(this.handleError));
  }

  getSingleDevice(id) {
    return this._http.get(`${this.baseURL}deviceDetails/${id}`).pipe(catchError(this.handleError));
   }

  private handleError(errorMsg: HttpErrorResponse) {
    let errMsg = '';
    if (errorMsg.error instanceof Error) {
      errMsg = errorMsg.error.message;
    } else {
      errMsg = errorMsg.error;
    }
    return throwError(errMsg);
  }
}
