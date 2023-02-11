import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import { SharedService } from '../shared.service';
import { SpyLocation } from '@angular/common/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HeaderInfo } from '../header.model';
import { Router } from '@angular/router';
import { MaterialModule } from '../material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LogoutComponent } from '../logout/logout.component';
import { NotificationService } from 'src/app/notification/notification.service';
import { CommonApiService } from '../common-api.service';
import { environment } from 'src/environments/environment';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
        RouterTestingModule,
        HttpClientTestingModule,
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
        HeaderComponent,
        LogoutComponent
      ],
      providers: [
        SharedService,
        NotificationService,
        SpyLocation,
        CommonApiService,
        {provide: 'UrlService', useClass: environment.apiServiceType},
        { provide: Router, useValue: routerSpy},
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    component.headerInfo = {
      isLoginPage: false
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to orders page once login is success', () => {
    const sampleHeadInfo: HeaderInfo = {
      headerName: 'Orders',
      isBackRequired: true,
      navigateUrl: 'orders',
      removeSharedData: 'orderDetails'
    };
    component.headerInfo = sampleHeadInfo;
    component.goBack();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['orders']);
  });
});
