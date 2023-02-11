import { TestBed, inject, async } from '@angular/core/testing';

import { SharedAuthGaurdService } from './shared-auth-gaurd.service';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedAuthService } from './shared-auth.service';
import { Router } from '@angular/router';
import { IdealService } from './ideal.service';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('IdealService', () => {
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        TranslateModule.forRoot(
          {
            loader: {
              provide: TranslateLoader,
              useFactory: HttpLoaderFactory,
              deps: [HttpClient]
            }
          }
        )
      ],
      providers: [
        IdealService,
        SharedAuthGaurdService,
        SharedAuthService,
        { provide: Router, useValue: routerSpy }
    ]
    });
  });

  beforeEach(() => {
  });

  it('should be created', inject([IdealService],
    (service: IdealService) => {
    expect(service).toBeTruthy();
  }));

});
