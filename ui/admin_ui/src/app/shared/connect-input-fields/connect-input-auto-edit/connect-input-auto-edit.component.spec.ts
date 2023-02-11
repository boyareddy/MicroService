import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectInputAutoEditComponent } from './connect-input-auto-edit.component';

describe('ConnectInputAutoEditComponent', () => {
  let component: ConnectInputAutoEditComponent;
  let fixture: ComponentFixture<ConnectInputAutoEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectInputAutoEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectInputAutoEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
