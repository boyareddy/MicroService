import { TestBed, inject } from '@angular/core/testing';

import { RsrService } from './rsr.service';

describe('RsrService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RsrService]
    });
  });

  it('should be created', inject([RsrService], (service: RsrService) => {
    expect(service).toBeTruthy();
  }));
});
