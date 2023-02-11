import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { SampleRunCardComponent } from './sample-run-card.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MaterialModule } from 'src/app/shared/material.module';
import { SingleRowDetailsComponent } from '../single-row-details/single-row-details.component';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { InlineEditComponent } from 'src/app/shared/inline-edit/inline-edit.component';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { FormsModule } from '@angular/forms';
import { WorkflowService } from 'src/app/workflow/workflow.service';
import { SharedService } from 'src/app/shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { NotificationService } from 'src/app/notification/notification.service';
import { environment } from 'src/environments/environment';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('SampleRunCardComponent', () => {
  let component: SampleRunCardComponent;
  let fixture: ComponentFixture<SampleRunCardComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        SampleRunCardComponent,
        SingleRowDetailsComponent,
        PluralCheckPipe,
        InlineEditComponent,
        DateTimeZoneDirective
      ],
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
        FormsModule,
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
        { provide: 'UrlService', useClass: environment.apiServiceType },
        { provide: Location, useValue: routerSpy },
        {
          provide: ActivatedRoute,
          useValue: { params: of({ runId: '1111' }) }
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SampleRunCardComponent);
    component = fixture.componentInstance;
    component.sampleruncard = {
      'assayType': 'NIPTDPCR',
      'comments': null,
      'deviceId': 'MP96-SR001',
      'deviceRunId': 'RND19196961772',
      'operatorName': 'admin',
      'processStepName': 'Library Preparation',
      'runCompletionTime': '2019-03-19T05:37:49.000Z',
      'runFlag': null,
      'runResultId': 13,
      'runStartTime': '2019-03-19T03:37:49.000Z',
      'uniqueReagentsAndConsumables': [
        {
          'attributeName': 'consumableDeviceFirstUseDate',
          'attributeValue': ''
        },
        {
          'attributeName': 'consumableDevicePartNumber',
          'attributeValue': '12314312'
        },
        {
          'attributeName': 'consumableDeviceExpiration',
          'attributeValue': ''
        },
        {
          'attributeName': 'seqKitPartNumber',
          'attributeValue': 'xyz'
        },
        {
          'attributeName': 'seqKitExpiration',
          'attributeValue': '2018-08-23 12:00:00'
        },
        {
          'attributeName': 'systemFluidExpiration',
          'attributeValue': '2018-08-23 12:00:00'
        }
      ],
      'totalSampleCount': 48,
      'outputContainerType': '96WellPlate',
      'protocolName': ['Cellular RNA LV 0.6.4'],
      'runsampledetails': [{
        'stripId': '',
        'sampledetails': [{
          'accessioningId': 'dryrun_demo_15',
          'assaytype': 'NIPTDPCR',
          'comments': 'Run-1 dpcr',
          'flags': null,
          'outputContainerId': '',
          'inputContainerId': 'dryrun_demo_15',
          'position': 'G2',
          'status': 'Failed',
          'processStepName': 'Library Preparation'
        }],
        'labelName': 'abSample',
        'labelImage': 'assets/Images/run_aborted.png'
      }],
      'status': 'Failed',
      'isOngoingProgressbarRequired': false,
      'runRemainingTime': '',
      'color': {
        'color': 'aborted',
        'transistionMode': 'determinate',
        'transistionValue': 100
      },
      'imagePath': 'LibraryPreparation/failed.png'
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get volume list by XHR call', fakeAsync(() => {
    const expOP = [
      {
        'deviceType': 'MP24',
        'processStepSeq': 1,
        'processStepName': 'NA Extraction',
        'inputContainerType': 'Plasma tube',
        'outputContainerType': '8-tube strip',
        'manualVerificationFlag': 'Y',
        'assayType': 'NIPTHTP',
        'sampleVolume': '200',
        'eluateVolume': '100',
        'reagent': null
      }
    ];
    const queryParam = {
      'assayTypeDetail': 'NIPTHTP',
      'deviceTypeID': 'MP24'
    };
    const workflowService = fixture.debugElement.injector.get(WorkflowService);
    fixture.detectChanges();
    spyOn(workflowService, 'getSampleVolumes').and.returnValue(of(expOP));
    tick();
    component.getVolumeList(queryParam);
    expect(JSON.stringify(component.samplevol)).toEqual(JSON.stringify(expOP[0]));
  }));

  it('should get the DeviceType details',
    () => {
      const assayType = 'NIPTDPCR';
      const processStepName = 'NA Extraction';
      expect(component.getDeviceType(assayType, processStepName)).toBeUndefined();
    });

  it('should call creaJsonForReagents for HTPSequencing', () => {
    const expOp = [
      {
        'attributeName': 'seqkit',
        'partNumber': 'xyz',
        'expiration': '2018-08-23 12:00:00'
      },
      {
        'attributeName': 'system',
        'expiration': '2018-08-23 12:00:00'
      }
    ];
    fixture.detectChanges();
    expect(component.creaJsonForReagents()).toBeUndefined();
  });

  it('should call createReagentsForNA_Extraction for NA_Extraction', () => {
    expect(component.createReagentsForNA_Extraction()).toBeUndefined();
  });

});
