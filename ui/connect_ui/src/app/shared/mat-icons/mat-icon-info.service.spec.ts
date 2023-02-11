import { TestBed, inject } from '@angular/core/testing';

import { MatIconInfoService } from './mat-icon-info.service';

describe('MatIconInfoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MatIconInfoService]
    });
  });

  it('should be created', inject([MatIconInfoService], (service: MatIconInfoService) => {
    expect(service).toBeTruthy();
  }));
});
