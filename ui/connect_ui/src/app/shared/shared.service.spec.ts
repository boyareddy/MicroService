import { TestBed, inject } from '@angular/core/testing';

import { SharedService } from './shared.service';

describe('SharedService', () => {
  let sharedService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SharedService]
    });
  });

  beforeEach(() => {
    sharedService = new SharedService();
  });

  afterEach(() => {
    sharedService = null;
  });

  it('should be created', inject([SharedService], (service: SharedService) => {
    expect(service).toBeTruthy();
  }));

  it('should set the data', () => {
    const key = 'key';
    const value = 'value';
    expect(sharedService.setData(key, value)).toBeUndefined();
  });

  it('should get the setting data', () => {
    const key = 'key';
    const value = 'value';
    sharedService.setData(key, value);
    expect(sharedService.getData('key')).toEqual('value');
  });

  it('should delete the setting data', () => {
    const key = 'key';
    const value = 'value';
    sharedService.setData(key, value);
    expect(sharedService.deleteData('key')).toBeUndefined();
  });
});
