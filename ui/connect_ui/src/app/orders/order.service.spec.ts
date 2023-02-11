import { TestBed, inject } from '@angular/core/testing';

import { OrderService } from './order.service';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { CommonApiService } from '../shared/common-api.service';
import { environment } from '../../environments/environment.dev';

describe('OrderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrderService, HttpClient, HttpHandler, CommonApiService, {provide: 'UrlService', useClass: environment.apiServiceType}]
    });
  });

  it('should be created', inject([OrderService], (service: OrderService) => {
    expect(service).toBeTruthy();
  }));
});
