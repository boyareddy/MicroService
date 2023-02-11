import { RsrLoginModule } from './rsr-login.module';

describe('RsrLoginModule', () => {
  let rsrLoginModule: RsrLoginModule;

  beforeEach(() => {
    rsrLoginModule = new RsrLoginModule();
  });

  it('should create an instance', () => {
    expect(rsrLoginModule).toBeTruthy();
  });
});
