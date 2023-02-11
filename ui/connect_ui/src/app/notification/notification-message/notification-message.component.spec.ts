import { async, ComponentFixture, TestBed, fakeAsync, tick, flushMicrotasks, flush } from '@angular/core/testing';
import { NotificationMessageComponent } from './notification-message.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from '../../shared/header/header.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { TranslateModule, TranslateLoader } from '../../../../node_modules/@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule, HttpHandler } from '@angular/common/http';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { ShowMoreComponent } from 'src/app/shared/show-more/show-more.component';
import { NotificationDescriptionComponent } from '../notification-description/notification-description.component';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { LogoutComponent } from '../../shared/logout/logout.component';
import { NotificationService } from 'src/app/notification/notification.service';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from '../../../environments/environment';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { Location } from '@angular/common';
import { of } from 'rxjs';



export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('NotificationMessageComponent', () => {
  let component: NotificationMessageComponent;
  let fixture: ComponentFixture<NotificationMessageComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        NotificationMessageComponent,
        HeaderComponent,
        EmptyDataCheck,
        DateTimeZoneDirective,
        ShowMoreComponent,
        NotificationDescriptionComponent,
        PluralCheckPipe,
        LogoutComponent
       ],
       imports : [
         MaterialModule,
         BrowserAnimationsModule,
        TranslateModule.forRoot(
          {
            loader: {
              provide: TranslateLoader,
              useFactory: HttpLoaderFactory,
              deps: [HttpClient]
            }
          }
        ),
      ],
      providers: [
        HttpClient,
        HttpHandler,
        NotificationService,
        CommonApiService,
        {
          provide: Location, useValue: routerSpy
        },
        {
          provide: Router, useValue: routerSpy
        },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get Notification List', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'Y', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}, {'id': 4165, 'createdBy': null, 'createdDateTime': '', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
  // tslint:disable-next-line:max-line-length
  const _nlF = [{'id': 4165, 'createdBy': null, 'createdDateTime': '', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getNotifications').and.returnValue(of(_nlAll));
    component.onLoadingNotificationList();
    fixture.detectChanges();
    tick(101);
    expect(JSON.stringify(component.dataSourceViewed)).toEqual(JSON.stringify(_nlAll));
  }));

  it('should get the dropdown values', fakeAsync(() => {
    const dropdownValue = [
      'All',
      'Device',
      'Orders',
      'Workflow'
    ];
    const dropdown = fixture.debugElement.injector.get(NotificationService);
    spyOn(dropdown, 'getAllDropdowns').and.returnValue(of(dropdownValue));
    component.getDropdownValue();
    fixture.detectChanges();
    tick(102);
    expect(JSON.stringify(component.dropdownResp)).toEqual(JSON.stringify(dropdownValue));
  }));

  it('should Change Topic "Workflow"', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}, {'id': 4165, 'createdBy': null, 'createdDateTime': '', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'Y', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getNotifications').and.returnValue(of(_nlAll));
    component.onLoadingNotificationList();
    fixture.detectChanges();

    // tslint:disable-next-line:max-line-length
    const _NLF = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}
    ];
    const event = { value: 'Workflow' } ;
    component.onChangeTopic(event);
    flushMicrotasks();
    fixture.detectChanges();
    tick(101);
    expect(JSON.stringify(component.dropResponse)).toEqual(JSON.stringify(_NLF));
  }));

  it('should Change Topic "All" selectedTab 0', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getNotifications').and.returnValue(of(_nlAll));
    component.onLoadingNotificationList();
    fixture.detectChanges();

    const event = { value: 'All' } ;
    component.onChangeTopic(event);
    flushMicrotasks();
    fixture.detectChanges();
    tick(101);
    expect(JSON.stringify(component.dropResponse)).toEqual(JSON.stringify(_nlAll));
  }));

  it('should Change Topic "All" selectedTab 1', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}, {'id': 4165, 'createdBy': null, 'createdDateTime': '', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'Y', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getNotifications').and.returnValue(of(_nlAll));
    component.displayedColumns = ['severity', 'topic', 'createdDateTime', 'title'];
    component.onLoadingNotificationList();
    fixture.detectChanges();

    const event = { value: 'All' } ;
    component.selectedTab = 1;
    tick();
    component.onChangeTopic(event);
    flushMicrotasks();
    fixture.detectChanges();
    tick(101);
    expect(JSON.stringify(component.dropResponse)).toEqual(JSON.stringify(_nlAll));
  }));

  it('should check onTabChanged by selectedTab 1"', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}, {'id': 4165, 'createdBy': null, 'createdDateTime': '', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'Y', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getNotifications').and.returnValue(of(_nlAll));
    component.onLoadingNotificationList();
    fixture.detectChanges();

    const event = { index: 1 } ;
    component.onTabChanged(event);
    flushMicrotasks();
    fixture.detectChanges();
    const mockDC = ['severity', 'topic', 'createdDateTime', 'title'];
    tick(401);
    expect(JSON.stringify(component.displayedColumns)).toEqual(JSON.stringify(mockDC));
  }));

  it('should check onTabChanged by selectedTab 0"', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}, {'id': 4165, 'createdBy': null, 'createdDateTime': '', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'Y', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getNotifications').and.returnValue(of(_nlAll));
    component.onLoadingNotificationList();
    fixture.detectChanges();

    const event = { index: 0 } ;
    component.onTabChanged(event);
    flushMicrotasks();
    fixture.detectChanges();
    const mockDC = ['select', 'severity', 'topic', 'createdDateTime', 'title'];
    tick(401);
    expect(JSON.stringify(component.displayedColumns)).toEqual(JSON.stringify(mockDC));
  }));

  xit('should Confirm the selected notification MS', fakeAsync(() => {
    // tslint:disable-next-line:max-line-length
    const _nlAll = [{'id': 4168, 'createdBy': null, 'createdDateTime': '2019-04-05T13:31:42.695Z', 'title': 'LP24 device offline', 'msg': 'The LP24 device kalpana_LP24 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'Y', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Workflow'}, {'id': 4165, 'createdBy': null, 'createdDateTime': '2019-04-05T13: 29: 49.249Z', 'title': 'MP96 device offline', 'msg': 'The MP96 device kalpana_MP96 is offline. Please establish connectivity to receive messages.', 'userId': null, 'viewed': 'N', 'viewedDateTime': null, 'severity': 'Error', 'topic': 'Device'}
  ];
    const _ns = fixture.debugElement.injector.get(NotificationService);
    spyOn(_ns, 'getAllDropdowns').and.returnValue(of([]));
    component.selectedNotfication = {};
    component.selectedNotfication['selected'] = [];
    component.selectedNotfication.selected = _nlAll;
    component.onConfirming();
    flushMicrotasks();
    fixture.detectChanges();
    expect(JSON.stringify(component.dropdownResp)).toEqual(JSON.stringify([]));
  }));

});
