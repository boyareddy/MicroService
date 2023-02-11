import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WarningRequiredOrderInfoComponent } from './warning-required-order-info.component';

describe('WarningRequiredOrderInfoComponent', () => {
  let component: WarningRequiredOrderInfoComponent;
  let fixture: ComponentFixture<WarningRequiredOrderInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WarningRequiredOrderInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WarningRequiredOrderInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
