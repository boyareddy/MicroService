import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegDeviceComponent } from './reg-device.component';

describe('RegDeviceComponent', () => {
  let component: RegDeviceComponent;
  let fixture: ComponentFixture<RegDeviceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegDeviceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegDeviceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
