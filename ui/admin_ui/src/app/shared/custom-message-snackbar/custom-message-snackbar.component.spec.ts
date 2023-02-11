import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMessageSnackbarComponent } from './custom-message-snackbar.component';

describe('CustomMessageSnackbarComponent', () => {
  let component: CustomMessageSnackbarComponent;
  let fixture: ComponentFixture<CustomMessageSnackbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomMessageSnackbarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomMessageSnackbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
