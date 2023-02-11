import { TestBed, inject } from '@angular/core/testing';

import { CommonApiService } from './common-api.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { environment } from '../../environments/environment';


describe('CommonApiService', () => {
  const createSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CommonApiService,
                  HttpClient,
                  HttpHandler,
        {
          provide: Router, useValue : createSpy
        },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    });
  });

  it('should be created', inject([CommonApiService],
    (service: CommonApiService) => {
    expect(service).toBeTruthy();
  }));

});
