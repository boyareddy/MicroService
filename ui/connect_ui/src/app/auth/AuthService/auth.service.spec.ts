import { TestBed, inject } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { environment } from 'src/environments/environment';

describe('AuthService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        HttpClient,
        HttpHandler,
        CommonApiService,
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    });
  });

  it('should be created', inject([AuthService], (service: AuthService) => {
    expect(service).toBeTruthy();
  }));
});
