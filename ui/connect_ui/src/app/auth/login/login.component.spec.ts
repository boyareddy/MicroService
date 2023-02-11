import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { HttpClient, HttpHandler } from '@angular/common/http';

import { LoginComponent } from './login.component';
import { HeaderComponent } from '../../shared/header/header.component';
import { AuthService } from '../AuthService/auth.service';
import { Location } from '@angular/common';
import { FooterComponent } from '../../orders/footer/footer.component';
import { SharedService } from '../../shared/shared.service';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { of } from 'rxjs';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from 'src/environments/environment';
import { NotificationService } from '../../notification/notification.service';
import { MaterialModule } from 'src/app/shared/material.module';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  // create new instance of FormBuilder
  const formBuilder: FormBuilder = new FormBuilder();

  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule.forRoot(
          {
            loader: {
              provide: TranslateLoader,
              useFactory: HttpLoaderFactory,
              deps: [HttpClient]
            }
          }
        )
      ],
      declarations: [
        LoginComponent,
        HeaderComponent,
        FooterComponent,
        LogoutComponent,
        PluralCheckPipe,
      ],
      providers: [
          AuthService,
          CommonApiService,
          NotificationService,
          HttpClient,
          HttpHandler, {
          provide: Router, useValue: routerSpy
        }, {
          provide: Location, useValue: routerSpy
        },
        { provide: FormBuilder, useValue: formBuilder },
        {provide: 'UrlService', useClass: environment.apiServiceType},
        SharedService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });


  // it('should check login service', fakeAsync(() => {
  //   component.loginFormGroup.get('userName').setValue('test');
  //   component.loginFormGroup.get('password').setValue('test');
  //   fixture.detectChanges();
  //   const _authService = fixture.debugElement.injector.get(AuthService);
  //   spyOn(_authService, 'checkAuth').and.returnValue(of(component.loginFormGroup.value));
  //   tick();
  //   fixture.detectChanges();
  //   component.onLogin();
  //   expect(component).toBeTruthy();
  // }));
});
