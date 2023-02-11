import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AsyncConfirmDialogComponent } from './async-confirm-dialog.component';

describe('AsyncConfirmDialogComponent', () => {
  let component: AsyncConfirmDialogComponent;
  let fixture: ComponentFixture<AsyncConfirmDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AsyncConfirmDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AsyncConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
