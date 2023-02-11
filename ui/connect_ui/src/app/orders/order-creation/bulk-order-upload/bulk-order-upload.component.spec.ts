import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkOrderUploadComponent } from './bulk-order-upload.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { MatCardModule,
        MatProgressBarModule,
        MatMenuModule,
        MatToolbarModule } from '@angular/material';
import { FileuploadErrorsComponent } from 'src/app/shared/fileupload-errors/fileupload-errors.component';
import { FileUploadModule } from 'ng2-file-upload';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { MdePopoverModule } from '@material-extended/mde';
import { OrderService } from '../../order.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { environment } from 'src/environments/environment';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClientTestingModule } from '../../../../../node_modules/@angular/common/http/testing';
import { BrowserAnimationsModule } from '../../../../../node_modules/@angular/platform-browser/animations';
import { MaterialModule } from 'src/app/shared/material.module';
import { of } from 'rxjs';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}


describe('BulkOrderUploadComponent', () => {
  let component: BulkOrderUploadComponent;
  let fixture: ComponentFixture<BulkOrderUploadComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulkOrderUploadComponent,
        HeaderComponent,
        FileuploadErrorsComponent,
        LogoutComponent
       ],
      imports : [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MaterialModule,
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
      providers : [
        OrderService,
        CommonApiService,
        {provide: 'UrlService', useClass: environment.apiServiceType},
        {
          provide: Router, useValue: routerSpy
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
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkOrderUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
