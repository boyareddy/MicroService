import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { SharedAuthService } from './shared-auth.service';
import { AppPropService } from './app-prop.service';
import { SharedService } from './shared.service';
import { IdealService } from './ideal.service';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AdminApiService } from './admin-api.service';
import { ex_URL } from '../utils/exceptionURL.const';
import { PermissionService } from './permission.service';

@Injectable()
export class AuthGuardService implements CanActivate {

  deviceTypes: any[] = [{ key: "lp24", value: "LP24" }, { key: "mp24", value: "MP24" }, { key: "mp96", value: "MP96" }, { key: "dpcr", value: "dPCR Analyzer" }];
  flags = {};
  isAuthenticated;
  appProperties;

  constructor(
    private _authService: SharedAuthService,
    private _apiPropSvc: AppPropService,
    private _router: Router,
    private _sharedSvc: SharedService,
    private _idealService: IdealService,
    private _adminApiService: AdminApiService,
    private _PS: PermissionService
  ) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    this.isAuthenticated = this._authService.isAuthenticated();
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    console.log(this.isAuthenticated, 'isAuthenticated');
    if (!this.isAuthenticated) {
      console.log(this.checkURL(), '$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$');
      if (!this.checkURL()) {
        window.location.href = `/connect_ui/#/login`;
        return of(false);
      } else {
        if (this.appProperties != null && this.appProperties !== undefined) {
          this._adminApiService.setProperties();
          return of(true);
        } else {
          console.log('as,basklfhasklnfaslfn');
          return this._apiPropSvc.getAppProperties().pipe(
            map((successData) => {
              console.log('API PROPS 222', successData);
              // this._PS.loadPermissions();
              // this.deviceTypes.forEach(deviceType => {
              //   this.loadFlags(deviceType, successData);
              // });
              this._sharedSvc.setSharedData('appProperties', successData);
              this._adminApiService.setProperties();
              // start the ideal service
              this._idealService.start();
              return true;
            }),
            catchError((error) => {
              if (!this.checkURL()) {
                window.location.href = `/connect_ui/#/login`;
                return of(false);
              }
            }));
        }
      }
    } else {
      if (this.appProperties != null && this.appProperties !== undefined) {
        this._adminApiService.setProperties();
        return of(true);
      } else {
        console.log('as,basklfhasklnfaslfn');
        return this._apiPropSvc.getAppProperties().pipe(
          map((successData) => {
            console.log('API PROPS', successData);
            this.deviceTypes.forEach(deviceType => {
              this.loadFlags(deviceType, successData);
            });
            this._sharedSvc.setSharedData('appProperties', successData);
            this._adminApiService.setProperties();
           // this._PS.loadPermissions();
            // start the ideal service
            this._idealService.start();
            return true;
          }),
          catchError((error) => {
            if (!this.checkURL()) {
              window.location.href = `/connect_ui/#/login`;
              return of(false);
            }
          }));
      }
    }
  }

  isInitialApiCallNeeded(): boolean{
    let isInitialApiCallNeeded = true;
    let noInitialApiCallForPages = ["forgot-password", "change-password", "reset-password"];
    let currentUrl = window.location.href.split("/");
    let currentPage = currentUrl[currentUrl.length - 1];

    if(noInitialApiCallForPages.indexOf(currentPage) > -1){
      isInitialApiCallNeeded = false;
    }

    return isInitialApiCallNeeded;
  }

  loadFlags(deviceType: any, apiProp: any){
    console.log("$$$$$$$$$$$$$$$$$$$$$$$", window.location.href.split("/"));
    if(this.isInitialApiCallNeeded()){
      this._apiPropSvc.getFlagsFromAmm(deviceType.value, apiProp).subscribe(successData => {
        if(successData){
          this.flags[deviceType.key] = {};
          successData.forEach(flag => {
            this.flags[deviceType.key][flag.flagCode] = flag.severity;
          });
        }
        this._sharedSvc.setSharedData('flags', this.flags);
      }, error => {
        console.log('Error while loading App Properties');
      });
    }
  }

  checkURL() {
    const cur_url_arr = window.location.href.split('/');
    console.log(cur_url_arr , 'cur_url_arr');
    const cur_url = cur_url_arr[cur_url_arr.length - 1];
    console.log(cur_url , 'cur_url');

    let counter = 0;
    let is_EX_URL = false;
    for (counter = 0; counter < ex_URL.length; counter++) {
      console.log(this._router.url, 'isToasterNeeded this._router.url');
      console.log(ex_URL[counter], 'isToasterNeeded NTROUTERURLS[counter]');
      console.log(cur_url.match(new RegExp(ex_URL[counter], 'g')), 'isToasterNeeded');
      if (ex_URL.length > 0 && cur_url && cur_url.match(new RegExp(ex_URL[counter], 'g')) !== null) {
        console.log(cur_url.match(new RegExp(ex_URL[counter], 'g')), '===============================');
          is_EX_URL = true;
          return of(true);
      }
    }
    if (!is_EX_URL) {
      return of(false);
    }
  }
}
