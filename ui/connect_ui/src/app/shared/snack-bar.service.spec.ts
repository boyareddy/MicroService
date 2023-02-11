import { TestBed, inject } from '@angular/core/testing';

import { SnackBarService } from './snack-bar.service';
import { MatSnackBarModule } from '@angular/material';

describe('SnackBarService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SnackBarService],
      imports: [MatSnackBarModule]
    });
  });

  it('should be created', inject([SnackBarService], (service: SnackBarService) => {
    expect(service).toBeTruthy();
  }));
});
