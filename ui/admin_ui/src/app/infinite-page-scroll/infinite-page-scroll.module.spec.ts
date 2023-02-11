import { InfinitePageScrollModule } from './infinite-page-scroll.module';

describe('InfinitePageScrollModule', () => {
  let infinitePageScrollModule: InfinitePageScrollModule;

  beforeEach(() => {
    infinitePageScrollModule = new InfinitePageScrollModule();
  });

  it('should create an instance', () => {
    expect(infinitePageScrollModule).toBeTruthy();
  });
});
