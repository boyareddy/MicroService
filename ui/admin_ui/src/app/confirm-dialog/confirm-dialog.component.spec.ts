import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDialogComponent } from './confirm-dialog.component';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { MatButtonModule,
  MatInputModule,
  MatCardModule,
  MatToolbarModule,
  MatIconModule,
  MatMenuModule,
  MatTabsModule,
  MatSelectModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatSlideToggleModule,
  MatProgressBarModule,
  MatStepperModule,
  MatRadioModule,
  MatGridListModule,
  MatTableModule
} from '@angular/material';
import { MdePopoverModule } from '@material-extended/mde';
import { RouterTestingModule } from '@angular/router/testing';

describe('ConfirmDialogComponent', () => {
  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;
  let dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);
  dialogRef.close.and.returnValue("");

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule
      ],
      declarations: [ ConfirmDialogComponent ],
      providers: [
         { provide: MAT_DIALOG_DATA, useValue: {} },
         { provide: MatDialogRef, userValue: dialogRef }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
