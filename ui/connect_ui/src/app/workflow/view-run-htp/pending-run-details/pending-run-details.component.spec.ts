import { async, ComponentFixture, TestBed } from '@angular/core/testing';


import { PendingRunDetailsComponent } from './pending-run-details.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { MaterialModule } from 'src/app/shared/material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SampleListComponent } from '../run-details/sample-list/sample-list.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ResponseProgressComponent } from 'src/app/shared/response-progress/response-progress.component';
import { ShowMoreComponent } from 'src/app/shared/show-more/show-more.component';
import { InlineEditComponent } from 'src/app/shared/inline-edit/inline-edit.component';
import { FlagPopupComponent } from 'src/app/shared/flag-popup/flag-popup.component';
import { FormsModule } from '@angular/forms';
import { WorkflowService } from '../../workflow.service';
import { SharedService } from 'src/app/shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { NotificationService } from 'src/app/notification/notification.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('PendingRunDetailsComponent', () => {
  let component: PendingRunDetailsComponent;
  let fixture: ComponentFixture<PendingRunDetailsComponent>;

  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PendingRunDetailsComponent,
        HeaderComponent,
        SampleListComponent,
        LogoutComponent,
        ResponseProgressComponent,
        ShowMoreComponent,
        InlineEditComponent,
        FlagPopupComponent
      ],
      imports: [
        BrowserAnimationsModule,
         MaterialModule,
         FormsModule,
         HttpClientModule,
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
        WorkflowService,
        SharedService,
        CommonApiService,
        NotificationService,
        {provide: 'UrlService', useClass: environment.apiServiceType},
        {
          provide: Location, useValue: routerSpy
        }
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PendingRunDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
