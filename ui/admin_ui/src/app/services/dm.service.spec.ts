import { TestBed, inject } from '@angular/core/testing';

import { DmService } from './dm.service';

describe('DmService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DmService]
    });
  });

  it('should be created', inject([DmService], (service: DmService) => {
    expect(service).toBeTruthy();
  }));
});
