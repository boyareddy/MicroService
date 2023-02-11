import { TestBed, inject, async } from '@angular/core/testing';

import { SharedAuthGaurdService } from './shared-auth-gaurd.service';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedAuthService } from './shared-auth.service';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('SharedAuthGaurdService', () => {
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
      ],
      providers: [
        SharedAuthGaurdService,
        SharedAuthService,
        { provide: Router, useValue: routerSpy }
    ]
    });
  });

  beforeEach(() => {
  });

  it('should be created', inject([SharedAuthGaurdService],
    (service: SharedAuthGaurdService) => {
    expect(service).toBeTruthy();
}));

  it('should return true when it is authenticated', () => {
  });

});
