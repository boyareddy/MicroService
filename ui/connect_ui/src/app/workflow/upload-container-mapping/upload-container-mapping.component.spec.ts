import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadContainerMappingComponent } from './upload-container-mapping.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';
import { FileUploadModule } from 'ng2-file-upload';
import { UploadErrorComponent } from '../upload-error/upload-error.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { WorkflowService } from '../workflow.service';
import { SharedService } from 'src/app/shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Subject } from 'rxjs';
import { Location } from '@angular/common';
import { NotificationService } from 'src/app/notification/notification.service';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

const mockRoute: any = { snapshot: {}};

mockRoute.parent = { params: new Subject<any>()};
mockRoute.params = new Subject<any>();
mockRoute.queryParams = new Subject<any>();


describe('UploadContainerMappingComponent', () => {
  let component: UploadContainerMappingComponent;
  let fixture: ComponentFixture<UploadContainerMappingComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        UploadContainerMappingComponent,
        HeaderComponent,
        UploadErrorComponent,
        LogoutComponent
      ],
      imports: [
        MaterialModule,
        HttpClientTestingModule,
        FileUploadModule,
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
        WorkflowService,
        SharedService,
        CommonApiService,
        NotificationService,
        { provide: Router, useValue: routerSpy },
        { provide: 'UrlService', useClass: environment.apiServiceType },
        { provide: Location, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: mockRoute }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadContainerMappingComponent);
    component = fixture.componentInstance;
    const properties = {
      'protocol': 'https',
      'host': 'www.qa-rocheconnect.com',
      'appPort': '8080',
      'contMapCSVPath': 'ui-conf/NIPT_dPCR_CSV_Template.csv',
      'bulkOrdCSVPath': 'ui-conf/csv/NIPTDPCR/BulkOrderUpload/Bulk upload - order entry - NIPT-dPCR.csv',
      'contMapCSVFile': 'NIPT_dPCR_CSV_Template.csv',
      'dynamicTemplate': 'Container_samples.csv',
      'csvTemplateRoot': 'ui-conf/csv',
      'secApi': {
        'module': 'security-web',
        'port': '90'
      },
      'rmmApi': {
        'module': 'rmm',
        'port': 86
      },
      'ammApi': {
        'module': 'amm',
        'port': 88
      },
      'ommApi': {
        'module': 'omm',
        'port': 96
      },
      'dmmApi': {
        'module': 'device-management',
        'port': 85
      },
      'admApi': {
        'module': 'admin-api',
        'port': 92
      },
      'admmApi': {
        'module': 'adm',
        'port': 97
      },
      'auditApi': {
        'module': 'audit-trail-boot',
        'port': 73
      },
      'sessionTimeOut': 300,
      'apiIntervalTime': 800000,

      'admin_ui': {
        'API_PORT': '92',
        'API_NAME': 'admin-api/json/',
        'SECURITY_API': 'security-web/json/',
        'DOMAIN': 'hcl.com',
        'ENTIRY_ID': 'PASUSER',
        'EMAIL_CODE': '/admin_ui/#/reset-password;code='
      },
      'csvConfig': {
        'url': '',
        'allowedMimeType': ['application/vnd.ms-excel'],
        'maxFileSize': 1000000,
        'displayFileSize': 1,
        'fileSizeUnit': 'mb'
      },
      'bulkOrderCSVConfig': {
        'url': '',
        'allowedMimeType': ['application/vnd.ms-excel'],
        'maxFileSize': 1000000
      },
      'bulkOrdCSVFile': 'Bulk_upload_order_entry_NIPT_dPCR.csv',
      'devAuth': { 'userDetails':
      { 'loginName': '', 'firstName': 'deviceuserabcdthreeo',
      'lastName': 'deviceuserabcdthreeo',
      'email': 'deviceabcdthreeo@hcl.com',
      'contact': [],
      'userPreferences': [{ 'preferenceKey': 'LOCALE', 'value': 'en_US' }] }, 'password': '' }
    };
    const sharedservice = fixture.debugElement.injector.get(SharedService);
    sharedservice.setData('appProperties', properties);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get the filesize by calling getFileSize by passing totalsize and unit as kb', () => {
    expect(Math.round(component.getFileSize(1000000, 'kb'))).toEqual(977);
  });

  it('should get the filesize by calling getFileSize by passing totalsize and unit as mb', () => {
    expect(Math.round(component.getFileSize(1000000, 'mb'))).toEqual(1);
  });

  it('should get the filesize by calling getFileSize by passing totalsize and unit as gb', () => {
    expect(Math.round(component.getFileSize(1000000, 'gb'))).toEqual(0);
  });

});
