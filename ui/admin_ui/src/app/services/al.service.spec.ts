import { TestBed, inject } from '@angular/core/testing';

import { AlService } from './al.service';

describe('AlService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AlService]
    });
  });

  it('should be created', inject([AlService], (service: AlService) => {
    expect(service).toBeTruthy();
  }));
});
