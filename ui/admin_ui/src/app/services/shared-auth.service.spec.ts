import { TestBed, inject } from '@angular/core/testing';

import { SharedAuthService } from './shared-auth.service';

describe('SharedAuthService', () => {
  let sharedAuthService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [sharedAuthService]
    });
  });

  beforeEach(() => {
    sharedAuthService = new sharedAuthService();
  });

  afterEach(() => {
    sharedAuthService = null;
    sessionStorage.removeItem('authStatus');
  });

  it('should be created', inject([SharedAuthService], (service: SharedAuthService) => {
    expect(service).toBeTruthy();
  }));

  it('should return true from isAuthenticated when there is a token', () => {
    sessionStorage.setItem('authStatus', 'true');
    expect(sharedAuthService.isAuthenticated()).toBeTruthy();
  });

  it('should return false from isAuthenticated when there is no token', () => {
    expect(sharedAuthService.isAuthenticated()).toBeFalsy();
  });

  it('should set the auth status', () => {
    expect(sharedAuthService.setAuthStatus()).toBeUndefined();
  });

  it('should delete the auth status', () => {
    expect(sharedAuthService.deleteAuthStatus()).toBeUndefined();
  });
});
