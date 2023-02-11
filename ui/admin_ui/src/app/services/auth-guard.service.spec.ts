import { TestBed, inject } from '@angular/core/testing';

import { AuthGuardService } from './auth-guard.service';
import { Router } from '@angular/router';

class MockAuthService {
  authenticated = false;
  isAuthenticated() {
    return this.authenticated;
  }
  setAuthStatus() {}
  deleteAuthStatus() {}

}

describe('AuthGuardService', () => {
  let authGuardService;
  let authService: MockAuthService;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthGuardService, {
        provide: Router, useValue: routerSpy
      }]
    });
  });

  beforeEach(() => {
    authService = new MockAuthService();
    authGuardService = new AuthGuardService(authService, null);
  });

  it('should be created', inject([AuthGuardService], (service: AuthGuardService) => {
    expect(service).toBeTruthy();
  }));

  it('should return true when it is authenticated', () => {
    authService.authenticated = true;
    expect(authGuardService.canActivate()).toBeTruthy();
  });
});
