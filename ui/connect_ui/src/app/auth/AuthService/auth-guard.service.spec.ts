import { TestBed, inject } from '@angular/core/testing';

import { AuthGuardService } from './auth-guard.service';
import { Router } from '@angular/router';
import { AppPropService } from 'src/app/shared/app-prop.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';


describe('AuthGuardService', () => {
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthGuardService, {
        provide: Router, useValue: routerSpy
      }, AppPropService, CommonApiService ]
    });
  });

  beforeEach(() => {
  });

  it('should be created', inject([AuthGuardService], (service: AuthGuardService) => {
    expect(service).toBeTruthy();
  }));

});
