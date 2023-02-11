import { TestBed, inject } from '@angular/core/testing';

import { NotificationtoasterService } from ./notificationtoaster.servicece';

describe('NotificationtoasterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotificationtoasterService]
    });
  });

  it('should be created', inject([NotificationtoasterService], (service: NotificationtoasterService) => {
    expect(service).toBeTruthy();
  }));
});
