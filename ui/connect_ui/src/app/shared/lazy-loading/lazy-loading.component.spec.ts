import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LazyLoadingComponent } from './lazy-loading.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

describe('LazyLoadingComponent', () => {
  let component: LazyLoadingComponent;
  let fixture: ComponentFixture<LazyLoadingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LazyLoadingComponent ],
      imports: [InfiniteScrollModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LazyLoadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
