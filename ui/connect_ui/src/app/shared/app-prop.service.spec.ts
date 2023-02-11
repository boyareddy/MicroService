import { TestBed, inject } from '@angular/core/testing';

import { AppPropService } from './app-prop.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AppPropService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports : [HttpClientTestingModule],
      providers: [AppPropService]

    });
  });

  it('should be created', inject([AppPropService], (service: AppPropService) => {
    expect(service).toBeTruthy();
  }));
});
