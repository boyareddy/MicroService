import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RsrComponent } from './rsr.component';

describe('RsrComponent', () => {
  let component: RsrComponent;
  let fixture: ComponentFixture<RsrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RsrComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RsrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
