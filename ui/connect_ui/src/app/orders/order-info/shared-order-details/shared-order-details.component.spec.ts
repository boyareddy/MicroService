import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedOrderDetailsComponent } from './shared-order-details.component';

describe('SharedOrderDetailsComponent', () => {
  let component: SharedOrderDetailsComponent;
  let fixture: ComponentFixture<SharedOrderDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedOrderDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharedOrderDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
