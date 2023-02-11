import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserListComponent } from './user-list.component';
import { MatExpansionModule, MatSnackBar } from '@angular/material';
import { UserList } from '../models/user.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpHandler } from '@angular/common/http';
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
import { AdminUiHeaderComponent } from '../admin-ui-header/admin-ui-header.component';
import { AdminApiService } from '../services/admin-api.service';
import { UserTableComponent } from '../user-table/user-table.component';
import { MdePopoverModule } from '@material-extended/mde';
import { RouterTestingModule } from '@angular/router/testing';

describe('ActiveUsersComponent', () => {
  let component: UserListComponent;
  let fixture: ComponentFixture<UserListComponent>;
  // tslint:disable-next-line:prefer-const
  let userList: UserList;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        FormsModule, ReactiveFormsModule, MatButtonModule,
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
        MatExpansionModule,
        MatSlideToggleModule,
        MatProgressBarModule,
        MatStepperModule,
        MatRadioModule,
        MatGridListModule,
        MatTableModule,
        MdePopoverModule,
        RouterTestingModule
      ],
      declarations: [ UserListComponent, AdminUiHeaderComponent, UserTableComponent ],
      providers: [
        AdminApiService, HttpClient, HttpHandler, MatSnackBar
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserListComponent);
    component = fixture.componentInstance;
    // componentSpec = new OrdersSpec();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

   it('should move to next', () => {
     component.nextStep();
     expect(component.step).toEqual(1);
   });

  it('should move to previous', () => {
    component.prevStep();
    expect(component.step).toEqual(-1);
  });

});
