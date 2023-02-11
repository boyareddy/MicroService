import { environment } from '../../environments/environment';
import { HttpErrorResponse, HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { throwError, interval} from 'rxjs';
import { catchError, switchMap, startWith } from 'rxjs/operators';
import { SharedService } from './shared.service';

@Injectable()
export class CommonApiService {
  [x: string]: any;

  // MOCK JSON Configs
  mockDataUri = './assets/JsonFiles/orders/';
  appPropVal;

  constructor(
    private _http: HttpClient,
    @Inject('UrlService') private urlService,
    private _sharedService: SharedService
  ) {}

  /**
    * getservice is a get method for all the API services
  */
  // get(serviceName: string, queryParam1?: any, queryParam2?: any) {
  //   let service_url = environment.apiFileName[serviceName];
  //   service_url = queryParam1 ? service_url.replace('queryParam1', queryParam1) : service_url;
  //   service_url = queryParam2 ? service_url.replace('queryParam2', queryParam2) : service_url;
  //   return this._http.get(service_url).pipe(catchError(this.handleError));
  // }

  get(serviceName: string, queryParam?: any, isIntervalEnable?: boolean) {
    console.log(this.urlService);
    console.log(this.urlService.urls);
    console.log(serviceName);
    let service_url = this.urlService.urls[serviceName];
    this.appPropVal = this._sharedService.getData('appProperties');
    const intervalTime = this.appPropVal ? this.appPropVal.apiIntervalTime : null;
    if (typeof queryParam === 'object') {
      Object.keys(queryParam).forEach(key => {
        service_url = queryParam ? service_url.replace(key, queryParam[key]) : service_url;
      });
    } else {
      service_url = queryParam ? service_url.replace('queryParam', encodeURIComponent(queryParam)) : service_url;
    }
    if (isIntervalEnable && intervalTime !== undefined && intervalTime !== null && intervalTime !== '') {
      const _interval = interval(intervalTime);
      return _interval
      .pipe(
        startWith(0),
        switchMap(() => this._http.get(service_url).pipe(catchError(this.handleError)))
      );
    } else {
      return this._http.get(service_url).pipe(catchError(this.handleError));
    }
  }

  getNoPipe(serviceName: string, queryParam?: any, isIntervalEnable?: boolean) {
    let service_url = this.urlService.urls[serviceName];
    this.appPropVal = this._sharedService.getData('appProperties');
    const intervalTime = this.appPropVal ? this.appPropVal.apiIntervalTime : null;
    if (typeof queryParam === 'object') {
      Object.keys(queryParam).forEach(key => {
        service_url = queryParam ? service_url.replace(key, queryParam[key]) : service_url;
      });
    } else {
      service_url = queryParam ? service_url.replace('queryParam', encodeURIComponent(queryParam)) : service_url;
    }
    if (isIntervalEnable && intervalTime !== undefined && intervalTime !== null && intervalTime !== '') {
      const _interval = interval(intervalTime);
      return _interval
      .pipe(
        startWith(0),
        switchMap(() => this._http.get(service_url))
      );
    } else {
      return this._http.get(service_url);
    }
  }

  getWithTextResponse(serviceName: string, queryParam?: any, isIntervalEnable?: boolean) {
    console.log(this.urlService);
    console.log(this.urlService.urls);
    console.log(serviceName);
    let service_url = this.urlService.urls[serviceName];
    this.appPropVal = this._sharedService.getData('appProperties');
    const intervalTime = this.appPropVal ? this.appPropVal.apiIntervalTime : null;
    if (typeof queryParam === 'object') {
      Object.keys(queryParam).forEach(key => {
        service_url = queryParam ? service_url.replace(key, queryParam[key]) : service_url;
      });
    } else {
      service_url = queryParam ? service_url.replace('queryParam', encodeURIComponent(queryParam)) : service_url;
    }
    if (isIntervalEnable && intervalTime !== undefined && intervalTime !== null && intervalTime !== '') {
      const _interval = interval(intervalTime);
      return _interval
      .pipe(
        startWith(0),
        switchMap(() => this._http.get(service_url).pipe(catchError(this.handleError)))
      );
    } else {
      return this._http.get(service_url, { responseType: 'text' }).pipe(catchError(this.handleError));
    }
  }

  getCSV(serviceName) {
    const service_url = this.urlService.urls[serviceName];
    return this._http.get(service_url, {responseType: 'text'});
  }

  getBulkOrderCSV(serviceName, queryParam) {
    let service_url = this.urlService.urls[serviceName];
    service_url = queryParam ? service_url.replace('queryParam', encodeURIComponent(queryParam)) : service_url;
    return this._http.get(service_url, {responseType: 'text'});
  }

  getCSVFromUrl(url: string) {
    return this._http.get(url, {responseType: 'text'}).pipe(catchError(this.handleError));
  }

  postWithTextRes(serviceName: string, queryParam?: any, jsonName?: any) {
    let service_url = this.urlService.urls[serviceName];
    if (environment.production) {
      service_url = queryParam ? service_url.replace('queryParam', queryParam) : service_url;
      // tslint:disable-next-line:max-line-length
      return this._http.post(service_url, JSON.stringify(queryParam), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }), responseType: 'text' }).pipe(catchError(this.handleError));
    } else {
      return this._http.get(service_url).pipe(catchError(this.handleError));
    }
  }

   /**
    * postservice is a post method for all the API services
  */
  post(serviceName: string, queryParam?: any, jsonName?: any) {
    let service_url = this.urlService.urls[serviceName];
    if (environment.production) {
      service_url = queryParam ? service_url.replace('queryParam', queryParam) : service_url;
      // tslint:disable-next-line:max-line-length
      return this._http.post(service_url, JSON.stringify(queryParam), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }).pipe(catchError(this.handleError));
    } else {
      return this._http.get(service_url).pipe(catchError(this.handleError));
    }
  }

  /**
    * postservice is a post method for all the API services
  */
 postNoPipe(serviceName: string, queryParam?: any, jsonName?: any) {
  let service_url = this.urlService.urls[serviceName];
  if (environment.production) {
    service_url = queryParam ? service_url.replace('queryParam', queryParam) : service_url;
    // tslint:disable-next-line:max-line-length
    return this._http.post(service_url, JSON.stringify(queryParam), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) });
  } else {
    return this._http.get(service_url).pipe(catchError(this.handleError));
  }
}

   /**
    * putservice is a put method for all the API services
  */
  put(serviceName: string, queryParam?: any) {
    let service_url = this.urlService.urls[serviceName];
    if (environment.production) {
      console.log(service_url, 'service');
      service_url = queryParam ? service_url.replace('queryParam', queryParam) : service_url;
      // tslint:disable-next-line:max-line-length
      return this._http.put(service_url, JSON.stringify(queryParam), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }), responseType: 'text' }).pipe(catchError(this.handleError));
    } else {
      return this._http.get(service_url).pipe(catchError(this.handleError));
    }
  }

   /**
    * putservice is a put method for all the API services
  */
 putNoPipe(serviceName: string, queryParam?: any) {
  let service_url = this.urlService.urls[serviceName];
  if (environment.production) {
    console.log(service_url, 'service');
    service_url = queryParam ? service_url.replace('queryParam', queryParam) : service_url;
    // tslint:disable-next-line:max-line-length
    return this._http.put(service_url, JSON.stringify(queryParam), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }), responseType: 'text'});
  } else {
    return this._http.get(service_url).pipe(catchError(this.handleError));
  }
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
