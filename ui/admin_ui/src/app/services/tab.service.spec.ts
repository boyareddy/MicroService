import { TestBed, inject } from '@angular/core/testing';

import { TabService } from './tab.service';
import { HttpClient } from '@angular/common/http';

describe('TabService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TabService, HttpClient]
    });
  });

  it('should be created', inject([TabService], (service: TabService) => {
    expect(service).toBeTruthy();
  }));
});
