import { TestBed, inject } from '@angular/core/testing';

import { LoclizeServiceService } from './loclize-service.service';
import { HttpClientModule } from '@angular/common/http';

describe('LoclizeServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ],
      providers: [
        LoclizeServiceService
      ]
    });
  });

  it('should be created', inject([LoclizeServiceService], (service: LoclizeServiceService) => {
    expect(service).toBeTruthy();
  }));
});
