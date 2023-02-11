import { tap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpRequest, HttpHandler } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { SharedAuthService } from './shared-auth.service';

@Injectable({
  providedIn: 'root'
})
export class HttpRequestInterceptorService implements HttpInterceptor {

  constructor(private _router: Router,
    private _authService: SharedAuthService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headerAuthKey = req.clone({ withCredentials: true });
    return next.handle(headerAuthKey).pipe(tap(success => {
      if (req.url.indexOf('ui-conf') === -1 && req.url.indexOf('assets') === -1) {
        this._authService.setAuthStatus();
      }
    }, error => {
      if (error.status === 401) {
        this._authService.deleteAuthStatus();
        window.location.href = `/connect_ui/#/login`;
      } else {
        this._authService.setAuthStatus();
      }
    }));
  }
}
