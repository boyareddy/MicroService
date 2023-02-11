import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationtoasterComponent } from './notificationtoaster.component';

describe('NotificationtoasterComponent', () => {
  let component: NotificationtoasterComponent;
  let fixture: ComponentFixture<NotificationtoasterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotificationtoasterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationtoasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
