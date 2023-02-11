import { async, ComponentFixture, TestBed, inject, tick, fakeAsync } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';

import { OrderInfoComponent } from './order-info.component';
import { OrderDetailsComponent } from './order-details/order-details.component';
import { AssayDetailsComponent } from './assay-details/assay-details.component';
import { PatientDetailsComponent } from './patient-details/patient-details.component';
import { HeaderComponent } from '../../shared/header/header.component';
import { SharedService } from '../../shared/shared.service';
import { OrderService } from '../order.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SpyLocation } from '@angular/common/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FooterComponent } from '../footer/footer.component';
import { of, Observable } from 'rxjs';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from '../../../environments/environment';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { MaterialModule } from 'src/app/shared/material.module';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { NotificationService } from 'src/app/notification/notification.service';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { setLocalObject, MULTI_NAV } from 'src/app/shared/utils/local-storage.util';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}


describe('OrderInfoComponent', () => {
  let component: OrderInfoComponent;
  let fixture: ComponentFixture<OrderInfoComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  const sampleOrderDetails = {
    'order': {
      'orderId': 10000003,
      'patientId': 103,
      'patientSampleId': 104,
      'accessioningId': '11111',
      'orderStatus': 'unassigned',
      'assayType': 'NIPT',
      'sampleType': 'Plasma',
      'retestSample': true,
      'orderComments': '',
      'activeFlag': 'Y',
      'createdBy': 'ADMIN',
      'createdDateTime': 1533895598755,
      'updatedBy': null,
      'updatedDateTime': null,
      'assay': {
          'patientAssayid': 103,
          'maternalAge': 11,
          'gestationalAgeWeeks': 5,
          'gestationalAgeDays': 4,
          'ivfStatus': '',
          'eggDonor': '',
          'fetusNumber': '',
          'collectionDate': 1533753000000,
          'receivedDate': 1533753000000,
          'testOptions': {
            'Fetal Sex': true,
            'Harmony': true,
            'SCAP': false,
            'MX': false
          }
      },
      'patient': {
          'patientId': 103,
          'patientLastName': '',
          'patientFirstName': '',
          'patientGender': '',
          'patientMedicalRecNo': '',
          'patientDOB': null,
          'patientContactNo': '',
          'treatingDoctorName': '',
          'treatingDoctorContactNo': '',
          'refClinicianName': '',
          'refClinicianFaxNo': '',
          'otherClinicianName': '',
          'otherClinicianFaxNo': '',
          'refClinicianClinicName': ''
      }
    }
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OrderInfoComponent,
        OrderDetailsComponent,
        AssayDetailsComponent,
        PatientDetailsComponent,
        HeaderComponent,
        FooterComponent,
        EmptyDataCheck,
        DateTimeZoneDirective,
        PluralCheckPipe
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MaterialModule,
        HttpClientModule,
        RouterTestingModule.withRoutes([]),
        TranslateModule.forRoot(
          {
            loader: {
              provide: TranslateLoader,
              useFactory: HttpLoaderFactory,
              deps: [HttpClient]
            }
          }
        ),
        BrowserAnimationsModule
      ],
      providers: [
        SharedService,
        SpyLocation,
        OrderService,
        CommonApiService,
        NotificationService,
        {
          provide: ActivatedRoute,
          useValue: { params: of({ id: '1111' }) }
        },
        {provide: 'UrlService', useClass: environment.apiServiceType},
        { provide: Router, useValue: routerSpy },
        {provide: Router, useClass: MockRouter}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderInfoComponent);
    component = fixture.componentInstance;
    setLocalObject(MULTI_NAV.ORDER_DETAIL_PREV, '/workflow/mapped-samples', 'WP123');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  /**
  * Check for the order id
  */
  it('should have the order id', () => {
    component.selectedListItem = '123';
    fixture.detectChanges();
    sessionStorage.setItem('orderStatus', 'inworkflow');
    fixture.detectChanges();
    component.ngOnInit();
    expect(component.selectedListItem).toEqual('123');
  });

  /**
   * Getting the OrderDetails from server
   */
  it(`should find the order which having id '10000003'`, fakeAsync(() => {
    const orderService = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    spyOn(orderService, 'getOrderInfo').and.returnValue(of(sampleOrderDetails));
    tick();
    component.getOrderDetails('10000003');
    expect(JSON.stringify(component.orderInfo)).toEqual(JSON.stringify(sampleOrderDetails));
  }));

  /**
   * Getting Assay Details from API
   * Getting Assay id from the assay details
   * Getting workflow steps from API
   */
  it(`should check all the assay types`, fakeAsync(() => {
    const orderService = fixture.debugElement.injector.get(OrderService);
    const sampleAssayTypes = [
      {
        'assayTypeId': 1,
        'assayType': 'NIPT',
        'assayVersion': 'V1',
        'workflowDefFile': 'NIPT_Wfm_v1.bpmn.xml',
        'workflowDefVersion': 'V1'
      }
    ];
    component.orderInfo = sampleOrderDetails;
    spyOn(orderService, 'getAssayTypes').and.returnValue(of(sampleAssayTypes));
    component.onGettingAssayTypes();
    tick();
    fixture.detectChanges();
    expect(JSON.stringify(component.assayTypeList)).toEqual(JSON.stringify(sampleAssayTypes));
  }));

  /**
   * Getting workflow steps from API
   * Getting workflow results from API
   * Generate the new JSON for UI
   */
  it(`should check for getWorkFlowSteps`, fakeAsync(() => {
    const orderService = fixture.debugElement.injector.get(OrderService);
    const sampleWorkflowSteps = [
      {'deviceType': 'MP24', 'processStepSeq': 1, 'processStepName': 'NA Extraction', 'manualVerificationFlag': 'Y'},
      {'deviceType': 'LP24', 'processStepSeq': 2, 'processStepName': 'LP Pre PCR', 'manualVerificationFlag': 'Y'},
      {'deviceType': 'LP24', 'processStepSeq': 3, 'processStepName': 'LP Post PCR/Pooling', 'manualVerificationFlag': 'Y'},
      {'deviceType': 'LP24', 'processStepSeq': 4, 'processStepName': 'LP Sequencing Preparation', 'manualVerificationFlag': 'Y'},
      {'deviceType': 'HTP', 'processStepSeq': 5, 'processStepName': 'Sequencing', 'manualVerificationFlag': 'Y'},
      {'deviceType': 'FORTE', 'processStepSeq': 6, 'processStepName': 'Analysis', 'manualVerificationFlag': 'Y'}
    ];
    component.orderInfo = sampleOrderDetails;
    fixture.detectChanges();
    spyOn(orderService, 'getWorkFlowSteps').and.returnValue(of(sampleWorkflowSteps));
    tick();
    component.getWorkFlowSteps();
    fixture.detectChanges();
    expect(JSON.stringify(component.workFlowSteps)).toEqual(JSON.stringify(sampleWorkflowSteps));

    const sampleWorkFlowResult = [
        {
          'sampleResultId': 456,
          'accesssioningId': '9876793',
          'orderId': 10000236,
          'outputContainerId': '362067364',
          'outputContainerPosition': '1',
          'outputKitId': null,
          'outputContainerType': null,
          'runResultId': 459,
          'deviceId': 'MP001-12',
          'processStepName': 'NA Extraction',
          'runStatus': 'completed',
          'runFlag': null,
          'operatorName': 'jimenj15',
          'comments': 'Comment on the sample result',
          'updatedBy': 'System',
          'runStartTime': '2018-08-31',
          'runCompletionTime': '2018-08-31',
          'runRemainingTime': null,
          'protocolName': [ ]
      },
      {
         'sampleResultId': 456,
         'accesssioningId': '9876793',
         'orderId': 10000236,
         'outputContainerId': '362067364',
         'outputContainerPosition': '1',
         'outputKitId': null,
         'outputContainerType': null,
         'runResultId': 459,
         'deviceId': 'MP001-12',
         'processStepName': 'NA Extraction',
         'runStatus': 'Inprogress',
         'runFlag': null,
         'operatorName': 'jimenj15',
         'comments': 'Comment on the sample result',
         'updatedBy': 'System',
         'runStartTime': '2018-08-31',
         'runCompletionTime': '2018-08-31',
         'runRemainingTime': null,
         'protocolName': [ ]
      }
    ];
    component.resultManagementDetails = sampleWorkFlowResult;
    spyOn(orderService, 'getWorkFlowResults').and.returnValue(of(sampleWorkFlowResult));
    tick();
    component.getWorkFlowResults();
    fixture.detectChanges();
    component.createJsonForWorkFlow();
    fixture.detectChanges();
    // Checking for completed status alone
    const sampleWorkFlowResult_1 = [
        {
          'sampleResultId': 456,
          'accesssioningId': '9876793',
          'orderId': 10000236,
          'outputContainerId': '362067364',
          'outputContainerPosition': '1',
          'outputKitId': null,
          'outputContainerType': null,
          'runResultId': 459,
          'deviceId': 'MP001-12',
          'processStepName': 'NA Extraction',
          'runStatus': 'completed',
          'runFlag': null,
          'operatorName': 'jimenj15',
          'comments': 'Comment on the sample result',
          'updatedBy': 'System',
          'runStartTime': '2018-08-31',
          'runCompletionTime': '2018-08-31',
          'runRemainingTime': null,
          'protocolName': [ ]
      },
      {
        'sampleResultId': 456,
        'accesssioningId': '9876793',
        'orderId': 10000236,
        'outputContainerId': '362067364',
        'outputContainerPosition': '1',
        'outputKitId': null,
        'outputContainerType': null,
        'runResultId': 459,
        'deviceId': 'MP001-12',
        'processStepName': 'NA Extraction',
        'runStatus': 'completed',
        'runFlag': null,
        'operatorName': 'jimenj15',
        'comments': 'Comment on the sample result',
        'updatedBy': 'System',
        'runStartTime': '2018-08-31',
        'runCompletionTime': '2018-08-31',
        'runRemainingTime': null,
        'protocolName': [ ]
      }
    ];
    component.resultManagementDetails = sampleWorkFlowResult_1;
    fixture.detectChanges();
    component.createJsonForWorkFlow();
    expect(JSON.stringify(component.resultManagementDetails)).toEqual(JSON.stringify(sampleWorkFlowResult_1));
  }));

});

class MockRouter {
  public ne = new NavigationEnd(0, 'http://localhost:4200/login', 'http://localhost:4200/login');
  public events = new Observable(observer => {
    observer.next(this.ne);
    observer.complete();
  });
}
