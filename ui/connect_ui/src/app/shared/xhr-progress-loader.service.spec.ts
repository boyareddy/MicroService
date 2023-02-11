import { TestBed, inject } from '@angular/core/testing';

import { XhrProgressLoaderService } from './xhr-progress-loader.service';
import { MatDialogModule, MatProgressSpinnerModule } from '@angular/material';

describe('XhrProgressLoaderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [XhrProgressLoaderService],
      imports: [MatDialogModule, MatProgressSpinnerModule]
    });
  });

  it('should be created', inject([XhrProgressLoaderService], (service: XhrProgressLoaderService) => {
    expect(service).toBeTruthy();
  }));
});
