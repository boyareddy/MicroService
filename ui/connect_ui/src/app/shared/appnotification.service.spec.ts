import { TestBed, inject } from '@angular/core/testing';

import { AppNotificationService } from './appnotification.service';

describe('AppNotificationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AppNotificationService
    ]
    });
  });

  beforeEach(() => {
  });

  it('should be created', inject([AppNotificationService],
    (service: AppNotificationService) => {
    expect(service).toBeTruthy();
  }));

});
