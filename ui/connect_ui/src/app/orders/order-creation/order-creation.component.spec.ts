import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderCreationComponent } from './order-creation.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ManualOrderCreationComponent } from './manual-order-creation/manual-order-creation.component';
import { BulkOrderUploadComponent } from './bulk-order-upload/bulk-order-upload.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MinMaxDirective } from 'src/app/shared/directives/min-max.directive';
import { TrimDirective } from 'src/app/shared/directives/trim-directive';
import { FileUploadModule } from 'ng2-file-upload';
import { FileuploadErrorsComponent } from 'src/app/shared/fileupload-errors/fileupload-errors.component';
import { Location } from '@angular/common';
import { RouterTestingModule } from '@angular/router/testing';
import { NotificationService } from 'src/app/notification/notification.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { environment } from 'src/environments/environment';
import { OrderService } from '../order.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('OrderCreationComponent', () => {
  let component: OrderCreationComponent;
  let fixture: ComponentFixture<OrderCreationComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OrderCreationComponent,
        HeaderComponent,
        LogoutComponent,
        ManualOrderCreationComponent,
        MinMaxDirective,
        BulkOrderUploadComponent,
        TrimDirective,
        FileuploadErrorsComponent
      ],
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
        HttpClientTestingModule,
        FormsModule,
        FileUploadModule,
        RouterTestingModule,
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
      providers: [
        OrderService,
        NotificationService,
        CommonApiService,
        {provide: 'UrlService', useClass: environment.apiServiceType},
        {
          provide: Location, useValue: routerSpy
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
