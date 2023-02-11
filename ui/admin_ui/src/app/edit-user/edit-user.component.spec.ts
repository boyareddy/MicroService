import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditUserComponent } from './edit-user.component';
import { MatSnackBar } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { MatButtonModule,
  MatInputModule,
  MatCardModule,
  MatToolbarModule,
  MatCheckboxModule,
  MatGridListModule,
  MatTableModule
} from '@angular/material';
import { MdePopoverModule } from '@material-extended/mde';
import { RouterTestingModule } from '@angular/router/testing';
import { MinMaxDirective } from '../directives/min-max.directive';

describe('EditUserComponent', () => {
  let component: EditUserComponent;
  let fixture: ComponentFixture<EditUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        FormsModule, ReactiveFormsModule, MatButtonModule,
        MatInputModule,
        MatCardModule,
        MatToolbarModule,
        MatCheckboxModule,
        MatGridListModule,
        MatTableModule,
        MdePopoverModule,
        RouterTestingModule
      ],
      declarations: [ EditUserComponent, MinMaxDirective ],
      providers: [
         HttpClient, HttpHandler, MatSnackBar
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
