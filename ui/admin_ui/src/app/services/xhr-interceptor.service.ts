import { tap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpRequest, HttpHandler } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class XhrInterceptorService implements HttpInterceptor {

  constructor(private _router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headerAuthKey = req.clone({ withCredentials: true });
    return next.handle(headerAuthKey).pipe(tap(success => {
      if (req.url.indexOf('ui-conf') === -1 && req.url.indexOf('assets') === -1) {
        sessionStorage.setItem('authStatus', 'true');
      }
    }, error => {
      if (error.status === 401) {
        sessionStorage.removeItem('authStatus');
        window.location.href = `/connect_ui/#/login`;
      } else {
        sessionStorage.setItem('authStatus', 'true');
      }
    }));
  }
}
