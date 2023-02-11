import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDashboardComponent } from './new-dashboard.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { NewCardComponent } from '../new-card/new-card.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { ResponseProgressComponent } from 'src/app/shared/response-progress/response-progress.component';
import { RunAchievedComponent } from '../run-archieved/run-archieved.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { MaterialModule } from 'src/app/shared/material.module';
import { RunArchievdTableComponent } from '../run-archievd-table/run-archievd-table.component';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { WorkflowService } from '../../workflow.service';
import { SharedService } from 'src/app/shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { environment } from 'src/environments/environment';
import { Subject } from 'rxjs';
import { Location } from '@angular/common';
import { NotificationService } from 'src/app/notification/notification.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

const mockRoute: any = { snapshot: {}};

mockRoute.parent = { params: new Subject<any>()};
mockRoute.params = new Subject<any>();
mockRoute.queryParams = new Subject<any>();


describe('NewDashboardComponent', () => {
  let component: NewDashboardComponent;
  let fixture: ComponentFixture<NewDashboardComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewDashboardComponent,
        HeaderComponent,
        NewCardComponent,
        ResponseProgressComponent,
        RunAchievedComponent,
        LogoutComponent,
        PluralCheckPipe,
        RunArchievdTableComponent,
        DateTimeZoneDirective,
       ],
       imports: [
        BrowserAnimationsModule,
         MaterialModule,
         RouterModule,
         HttpClientModule,
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
        {
          provide: Router, useValue: routerSpy
        },
        { provide: ActivatedRoute, useValue: mockRoute },
        {provide: 'UrlService', useClass: environment.apiServiceType},
        {
          provide: Location, useValue: routerSpy
        }
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should cover ngOnit', () => {
    component.ngOnInit();
  });
});
