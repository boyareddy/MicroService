import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { WorkFlowComponent } from './work-flow.component';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import * as moment from 'moment-timezone';
import { OrderService } from '../../order.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CommonApiService } from '../../../shared/common-api.service';
import { of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { environment } from '../../../../environments/environment';
import { MaterialModule } from 'src/app/shared/material.module';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { FieldDetailsComponent } from '../field-details/field-details.component';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('WorkFlowComponent', () => {
  let component: WorkFlowComponent;
  let fixture: ComponentFixture<WorkFlowComponent>;

  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        WorkFlowComponent,
        EmptyDataCheck,
        FieldDetailsComponent,
        DateTimeZoneDirective
      ],
      imports : [
        MaterialModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
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
       providers : [
        OrderService,
        CommonApiService,
        {
          provide: Router, useValue: routerSpy
        },
        {provide: 'UrlService', useClass: environment.apiServiceType}
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkFlowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return the convert the time zone', () => {
    const gettingDate = 1536142101437;
    const result = component.getTimeZone(gettingDate);

    const currentTime = new Date();
    const timeZone = moment.tz.guess();
    const timeZoneOffset = currentTime.getTimezoneOffset();
    const dateZone = moment.tz.zone(timeZone).abbr(timeZoneOffset);

    expect(result).toBe(dateZone);
  });

  it('should return the convert the time zone to be undefined', () => {
    const gettingDate = null;
    const result = component.getTimeZone(gettingDate);

    const currentTime = new Date();
    const timeZone = moment.tz.guess();
    const timeZoneOffset = currentTime.getTimezoneOffset();
    const dateZone = moment.tz.zone(timeZone).abbr(timeZoneOffset);

    expect(result).toBe(undefined);
  });

   /**
   * Find next or prev Order
   */
  // it('should get molicular id for sequencing.', fakeAsync(() => {
  //   const workFlowResult = [
  //     {
  //       'processStepName' : 'NA Extraction',
  //       'runStatus' : 'completed',
  //       'outputContainerId': 'Single pool tube',
  //       'outputKitId': '1234567',
  //       'runFlag': 'na',
  //       'updatedBy': 'MariaG',
  //       'runCompletionTime': '06-MAY-2018 01:35:43 PDT',
  //       'protocolName': 'NIPT v1.0.0',
  //       'comments': 'It is a long established'
  //     },
  //     {
  //       'processStepName' : 'LP Pre PCR',
  //       'runStatus' : 'completed',
  //       'outputContainerId': 'Single pool tube',
  //       'outputKitId': '1234567',
  //       'runFlag': 'na',
  //       'updatedBy': 'MariaG',
  //       'runCompletionTime': '06-MAY-2018 01:35:43 PDT',
  //       'protocolName': 'NIPT v1.0.0',
  //       'comments': 'It is a long established'
  //     },
  //     {
  //       'processStepName' : 'LP Post PCR/Pooling',
  //       'runStatus' : 'completed',
  //       'outputContainerId': 'Single pool tube',
  //       'outputKitId': '1234567',
  //       'runFlag': 'na',
  //       'updatedBy': 'MariaG',
  //       'runCompletionTime': '06-MAY-2018 01:35:43 PDT',
  //       'protocolName': 'NIPT v1.0.0',
  //       'comments': 'It is a long established'
  //     },
  //     {
  //       'processStepName' : 'LP Sequencing Prep',
  //       'runStatus' : 'completed',
  //       'outputContainerId': 'Single pool tube',
  //       'outputKitId': '1234567',
  //       'runFlag': 'na',
  //       'updatedBy': 'MariaG',
  //       'runCompletionTime': '06-MAY-2018 01:35:43 PDT',
  //       'protocolName': 'NIPT v1.0.0',
  //       'comments': 'It is a long established'
  //     },
  //     {
  //       'processStepName' : 'Sequencing',
  //       'runStatus' : 'completed',
  //       'outputContainerId': 'Single pool tube',
  //       'outputKitId': '1234567',
  //       'runFlag': 'na',
  //       'updatedBy': 'MariaG',
  //       'runCompletionTime': '06-MAY-2018 01:35:43 PDT',
  //       'protocolName': 'NIPT v1.0.0',
  //       'plateType': 'A',
  //       'plateLocation': 'A1',
  //       'comments': 'It is a long established'
  //     }
  //   ];
  //   const molicularId = [
  //     {
  //         'molecularId': 'MID1',
  //         'plateType': 'A',
  //         'plateLocation': 'A1',
  //         'assayTypeId': 1
  //     }
  // ];
  //   const orderService = fixture.debugElement.injector.get(OrderService);
  //   component.workFlowDetails = workFlowResult;
  //   spyOn(orderService, 'getMolicularID').and.returnValue(of(molicularId));
  //   tick(1000);
  //   fixture.detectChanges();
  //   component.ngOnChanges();
  //   fixture.detectChanges();
  //   expect(component.molicularID).toEqual(molicularId);
  // }));

  it('should not get molecular id for sequencing.', () => {
    const workFlowResult = null;
    component.workFlowDetails = workFlowResult;
    component.ngOnChanges();
    fixture.detectChanges();
    expect(component.molicularID).toBe(undefined);
  });

  it('should not get molecular id without plate type for sequencing.', () => {
    const workFlowResult = [
      {
        'processStepName' : 'Sequencing',
        'runStatus' : 'completed',
        'outputContainerId': 'Single pool tube',
        'outputKitId': '1234567',
        'runFlag': 'na',
        'updatedBy': 'MariaG',
        'runCompletionTime': '06-MAY-2018 01:35:43 PDT',
        'protocolName': 'NIPT v1.0.0',
        'plateType': null,
        'plateLocation': 'A1',
        'comments': 'It is a long established'
      }
    ];
    component.workFlowDetails = workFlowResult;
    component.ngOnChanges();
    fixture.detectChanges();
    expect(component.molicularID).toBe(undefined);
  });
});
