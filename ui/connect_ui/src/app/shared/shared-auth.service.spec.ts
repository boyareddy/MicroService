import { TestBed, inject } from '@angular/core/testing';

import { SharedAuthService } from './shared-auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('SharedAuthService', () => {
  let sharedAuthService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SharedAuthService]
    });
  });

  beforeEach(() => {
  });

  afterEach(() => {
    sharedAuthService = new SharedAuthService(null, null);
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
