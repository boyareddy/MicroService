import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SettingsComponent } from './settings.component';
import { MatExpansionModule, MatSnackBar } from '@angular/material';
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
  MatGridListModule,
  MatTableModule
} from '@angular/material';
import { AdminUiHeaderComponent } from '../admin-ui-header/admin-ui-header.component';
import { UserTableComponent } from '../user-table/user-table.component';
import { MdePopoverModule } from '@material-extended/mde';
import { RouterTestingModule } from '@angular/router/testing';
import { MyProfileComponent } from '../my-profile/my-profile.component';
import { UserListComponent } from '../user-list/user-list.component';
import { MinMaxDirective } from '../directives/min-max.directive';

describe('SettingsComponent', () => {
  let component: SettingsComponent;
  let fixture: ComponentFixture<SettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        FormsModule, ReactiveFormsModule, MatButtonModule,
        MatInputModule,
        MatCardModule,
        MatToolbarModule,
        MatMenuModule,
        MatTabsModule,
        MatExpansionModule,
        MatGridListModule,
        MatTableModule,
        MdePopoverModule,
        RouterTestingModule
      ],
      declarations: [
        SettingsComponent,
        AdminUiHeaderComponent,
        MyProfileComponent,
        UserListComponent,
        UserTableComponent,
        MinMaxDirective],
      providers: [
         HttpClient, HttpHandler, MatSnackBar
      ]
    })
    .compileComponents();
  }));


  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
