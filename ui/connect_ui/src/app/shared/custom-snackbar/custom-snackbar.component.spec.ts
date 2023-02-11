import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomSnackbarComponent } from './custom-snackbar.component';
import { MatSnackBarModule, MAT_SNACK_BAR_DATA } from '@angular/material';

describe('CustomSnackbarComponent', () => {
  let component: CustomSnackbarComponent;
  let fixture: ComponentFixture<CustomSnackbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomSnackbarComponent ],
      imports: [MatSnackBarModule],
      providers: [
        { provide: MAT_SNACK_BAR_DATA, useValue: {} }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomSnackbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
