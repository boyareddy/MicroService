import { TestBed, inject, async } from '@angular/core/testing';

import { PermissionService } from './permission.service';

describe('PermissionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PermissionService
    ]
    });
  });

  beforeEach(() => {
  });

  it('should be created', inject([PermissionService],
    (service: PermissionService) => {
    expect(service).toBeTruthy();
  }));

});
