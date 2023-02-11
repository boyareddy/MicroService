import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkUploadPreviewComponent } from './bulk-upload-preview.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { MatTableModule } from '@angular/material';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ShowCenterEllipsesComponent } from 'src/app/shared/show-center-ellipses/show-center-ellipses.component';
import { ShowMoreComponent } from 'src/app/shared/show-more/show-more.component';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { MaterialModule } from '../../../shared/material.module';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { OrderService } from 'src/app/orders/order.service';
import { CommonApiService } from '../../../shared/common-api.service';
import { environment } from '../../../../environments/environment';
import { Location } from '@angular/common';
import { NotificationService } from '../../../notification/notification.service';
import { FileUploadModule } from '../../../../../node_modules/ng2-file-upload';
import { of } from 'rxjs';
import { RouterTestingModule } from '../../../../../node_modules/@angular/router/testing';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('BulkUploadPreviewComponent', () => {
  let component: BulkUploadPreviewComponent;
  let fixture: ComponentFixture<BulkUploadPreviewComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  const router = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        BulkUploadPreviewComponent,
        ShowMoreComponent,
        HeaderComponent,
        ShowCenterEllipsesComponent,
        DateTimeZoneDirective,
        LogoutComponent,
       ],
      imports: [
        MatTableModule,
        MaterialModule,
        FileUploadModule,
        RouterTestingModule,
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
        HttpClient,
        HttpHandler,
        OrderService,
        NotificationService,
        CommonApiService,
        {
          provide: Location, useValue: routerSpy
        },
        {
          provide: ActivatedRoute,
            useValue: {
              params: of({
                id: 123,
                step: 0
              })
            }
        },
        {
          provide: Router, useValue: routerSpy
        },
        {
          provide: Router, useValue: router
        },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkUploadPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
