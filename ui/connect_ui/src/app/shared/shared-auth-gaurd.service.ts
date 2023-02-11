import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { SharedAuthService } from '../shared/shared-auth.service';

@Injectable({
  providedIn: 'root'
})
export class SharedAuthGaurdService {

  constructor(private _authService: SharedAuthService,
              private _router: Router
  ) { }

  canActivate(): boolean {
    const isAuthenticated: boolean = this._authService.isAuthenticated();
    if (!isAuthenticated) {
      window.location.href = `/connect_ui/#/login`;
      return false;
    } else {
      return true;
    }
  }
}
