import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotOngoingComponent } from './not-ongoing.component';

describe('NotOngoingComponent', () => {
  let component: NotOngoingComponent;
  let fixture: ComponentFixture<NotOngoingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotOngoingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotOngoingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
