import { TestBed, inject } from '@angular/core/testing';

import { SharedService } from './shared.service';
import { HttpClient } from '@angular/common/http';

describe('SharedService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SharedService, HttpClient]
    });
  });

  it('should be created', inject([SharedService], (service: SharedService) => {
    expect(service).toBeTruthy();
  }));
});
