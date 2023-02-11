import { TestBed, inject } from '@angular/core/testing';

import { HttpRequestInterceptorService } from './http-request-interceptor.service';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('HttpRequestInterceptorService', () => {
  const createSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        HttpRequestInterceptorService,
        {
          provide: Router, useValue : createSpy
        }
      ],
      imports: [
        HttpClientTestingModule,
      ]
    });
  });

  it('should be created', inject([HttpRequestInterceptorService],
    (service: HttpRequestInterceptorService) => {
    expect(service).toBeTruthy();
  }));
});
