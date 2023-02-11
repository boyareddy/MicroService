import { SharedAuthService } from '../../shared/shared-auth.service';
import { Injectable, Inject } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AppPropService } from '../../shared/app-prop.service';
import { SharedService } from '../../shared/shared.service';
import { IdealService } from '../../shared/ideal.service';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { ex_URL } from '../../shared/utils/exceptionURL.const';
import { ApiUrlService } from 'src/app/shared/service-urls/apiurl.service';

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(
    private _authService: SharedAuthService,
    private _apiPropSvc: AppPropService,
    private _router: Router,
    private _sharedSvc: SharedService,
    private _idealService: IdealService,
    @Inject('UrlService') private _urlSvc: ApiUrlService
  ) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const isAuthenticated: boolean = this._authService.isAuthenticated();
    const appProperties = this._sharedSvc.getData('appProperties');
    console.log(isAuthenticated, 'isAuthenticated');
    if (appProperties != null && appProperties !== undefined) {
      if (!isAuthenticated) {
        if (!this.checkURL()) {
          window.location.href = `/connect_ui/#/login`;
          return of(false);
        } else {
          return of(true);
        }
      } else {
        return of(true);
      }
    } else {
      return this._apiPropSvc.getAppProperties().pipe(
        map((successData) => {
          console.log('API PROPS', successData);
          this._sharedSvc.setData('appProperties', successData);
          //this._urlSvc.setProperties();
          // start the ideal service
          this._idealService.start();
          if (!isAuthenticated) {
            if (!this.checkURL()) {
              window.location.href = `/connect_ui/#/login`;
              return true;
            } else {
              return true;
            }
          } else {
            return true;
          }
        }),
       catchError((error) => {
        if (!this.checkURL()) {
          window.location.href = `/connect_ui/#/login`;
          return of(false);
        }
       }));
    }
  }

  checkURL() {
    const cur_url_arr = window.location.href.split('/');
    const cur_url = cur_url_arr[cur_url_arr.length - 1];
    if (ex_URL.length > 0 && cur_url && ex_URL.indexOf(cur_url) !== -1) {
      return true;
    } else {
      return false;
    }
  }
}
