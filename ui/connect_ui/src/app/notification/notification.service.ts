import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError } from 'rxjs';
import { CommonApiService } from '../shared/common-api.service';

@Injectable()
export class NotificationService {

  // MOCK JSON Configs
  mockDataUri = './assets/JsonFiles/orders/';

  constructor(private _http: HttpClient, private _commonapiservice: CommonApiService) {}

  /**
   * Get Inworkflow order count details
   */
  getNotifications() {
    return this._commonapiservice.get('getNotification', undefined, true);
  }

  getAllDropdowns() {
    return this._commonapiservice.get('getAllDropdowns', undefined, true);
  }

  updateNotification(notification) {
    return this._commonapiservice.put('upadateNotification', notification);
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
