import { TestBed, inject } from '@angular/core/testing';

import { XhrProgressLoaderService } from './xhr-progress-loader.service';

describe('XhrProgressLoaderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [XhrProgressLoaderService]
    });
  });

  it('should be created', inject([XhrProgressLoaderService], (service: XhrProgressLoaderService) => {
    expect(service).toBeTruthy();
  }));
});
