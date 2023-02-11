import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { handleError } from '../utils/misc.util';
import { catchError } from 'rxjs/operators';
import { SharedService } from '../services/shared.service';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {

  connectSwInfo: any;
  parsedConnectSwInfo: any;
  appProperties: any;
  admApiEnd: string;
  constructor(private _httpClient: HttpClient, private _sharedSvc: SharedService) {
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    // tslint:disable-next-line:max-line-length
    this.admApiEnd = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admmApi.port}/${this.appProperties.admmApi.module}`;
  }


  ngOnInit() {
   // tslint:disable-next-line:max-line-length
    this._httpClient.get(`${this.admApiEnd}/json/rest/api/v1/getversioninfo`, { withCredentials: true}).pipe(catchError(handleError)).subscribe(aboutInfo => {
    this.parsedConnectSwInfo = this.getFormat(aboutInfo);
    });
  }


  getFormat(data: any) {
    const format: any = [];
    const keys = Object.keys(data);
    keys.forEach(key => {
      let subKeys;
      // tslint:disable-next-line:prefer-const
      let object = {swGroup: key, softwares: []};
      console.log(object , 'object');
      if (typeof data[key] !== 'string') {
        subKeys = Object.keys(data[key]);
        subKeys.forEach(subKey => {
          object.softwares.push({swKey: subKey, swName: subKey, version: data[key][subKey]});
        });
      } else {
        object.softwares.push({swKey: key, swName: key, version: data[key]});
      }
      console.log(object , 'obj^****');
      format.push(object);
    });
    console.log('Array', format);
    format.sort((a, b) => (a.softwares.length < b.softwares.length) ? -1 : (a.softwares.length > b.softwares.length) ? 1 : 0);
    return format;
  }

}
