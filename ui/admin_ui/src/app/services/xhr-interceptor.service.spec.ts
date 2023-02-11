import { TestBed, inject } from '@angular/core/testing';

import { XhrInterceptorService } from './xhr-interceptor.service';

describe('XhrInterceptorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [XhrInterceptorService]
    });
  });

  it('should be created', inject([XhrInterceptorService], (service: XhrInterceptorService) => {
    expect(service).toBeTruthy();
  }));
});
