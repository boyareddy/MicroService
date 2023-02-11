import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUserComponent } from './add-user.component';
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
import { AdminApiService } from '../services/admin-api.service';
import { of, Observable, throwError } from 'rxjs';
import { Router } from '@angular/router';


describe('AddUserComponent', () => {
  let component: AddUserComponent;
  let fixture: ComponentFixture<AddUserComponent>;
  const adminService = jasmine.createSpyObj('AdminApiService', ['addUser']);
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

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
      declarations: [ AddUserComponent, MinMaxDirective ],
      providers: [
         HttpClient, HttpHandler, MatSnackBar, {provide: AdminApiService, useValue: adminService}, { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();
  }));


  beforeEach(() => {
    fixture = TestBed.createComponent(AddUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('method: setStep', () => {
    component.setStep(2);
    expect(component.step).toBe(2);
  });

  it('method: nextStep', () => {
    component.step = 4;
    component.nextStep();
    expect(component.step).toBe(5);
  });

  it('method: nextStep', () => {
    component.step = 4;
    component.prevStep();
    expect(component.step).toBe(3);
  });

  it('method: onAddingUser > Success', () => {
    const addUserFormData = {
      value: {
        loginName: 'admin',
        userRole: ['Admin']
      }
    };
    adminService.addUser.and.returnValue(of({}));
    component.onAddingUser(addUserFormData);

    // expect(component.step).toBe(3);
  });

  it('method: onAddingUser > Error > IF > IF', () => {
    const addUserFormData = {
      value: {
        loginName: 'admin',
        userRole: ['Admin']
      }
    };
    adminService.addUser.and.returnValue(throwError({status: 2502, message: 'Failed'}));
    component.onAddingUser(addUserFormData);
  });

  // it('method: onAddingUser > Error > IF > ELSE', () => {
  //   let addUserFormData = {
  //     value: {
  //       loginName: "admin",
  //       userRole: ['Admin']
  //     }
  //   };
  //   adminService.addUser.and.returnValue(throwError({status: 500, message: "Failed"}));
  //   component.onAddingUser(addUserFormData);
  // });

  // it('method: onSelectUserRole', () => {
  //   component.onSelectUserRole(of({}));
  // });

  // it('method: goBack', () => {
  //   const _routerSpy = routerSpy.navigate as jasmine.Spy;
  //   fixture.detectChanges();
  //   fixture.componentInstance.goBack();
  //   expect (_routerSpy).toHaveBeenCalledTimes(2);
  // });

  // it('method: goBack', () => {
  //   component.addUserForm = {
  //     get: (test) => {
  //       return true;
  //     }
  //   }
  //   component.onCheckBoxValidation();
  // });
});
