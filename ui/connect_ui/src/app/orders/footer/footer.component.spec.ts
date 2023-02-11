import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { FooterComponent } from './footer.component';
import { MatToolbarModule } from '@angular/material';
import { SharedService } from '../../shared/shared.service';
import { OrderService } from '../order.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from '../../../environments/environment';
import { AppPropService } from '../../shared/app-prop.service';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FooterComponent,
        PluralCheckPipe,
      ],
      imports: [
        MatToolbarModule,
        HttpClientTestingModule,
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
        SharedService,
        OrderService,
        CommonApiService,
        { provide : Router , useValue : routerSpy },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  /**
   * Getting the Unassigned Orders List.
   */
  it('should pass the API call for getUnassignedOrdersList', fakeAsync(() => {
    fixture = TestBed.createComponent(FooterComponent);
    const orderList = [
      {
         'orderId': 10000074,
         'patientId': 405,
         'patientSampleId': 458,
         'accessioningId': '123456789',
         'orderStatus': 'unassigned',
         'assayType': 'NIPT',
         'sampleType': 'PLASMA',
         'retestSample': false,
         'orderComments': 'NIPT Test1',
         'activeFlag': 'Y',
         'createdBy': 'ADMIN',
         'createdDateTime': 1533967421530,
         'updatedBy': null,
         'updatedDateTime': null,
         'assay': null,
         'patient': null
      }
   ];
    const app = fixture.debugElement.componentInstance;
    const orderService = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(orderService, 'getUnassignedOrdersList').and.returnValue(of(orderList));
    app.ngOnInit();
  }));


  /**
   * Find next or prev Order
   */
  it('should get previous order and navigate to that order.', fakeAsync(() => {
    const orderList = [
      {
         'orderId': 10000074,
         'patientId': 405,
         'patientSampleId': 458,
         'accessioningId': '123456789',
         'orderStatus': 'unassigned',
         'assayType': 'NIPT',
         'sampleType': 'PLASMA',
         'retestSample': false,
         'orderComments': 'NIPT Test1',
         'activeFlag': 'Y',
         'createdBy': 'ADMIN',
         'createdDateTime': 1533967421530,
         'updatedBy': null,
         'updatedDateTime': null,
         'assay': null,
         'patient': null
      },
      {
        'orderId': 10000073,
        'patientId': 404,
        'patientSampleId': 456,
        'accessioningId': '123456789',
        'orderStatus': 'unassigned',
        'assayType': 'NIPT',
        'sampleType': 'PLASMA',
        'retestSample': false,
        'orderComments': 'NIPT Test1',
        'activeFlag': 'Y',
        'createdBy': 'ADMIN',
        'createdDateTime': 1533967340760,
        'updatedBy': null,
        'updatedDateTime': null,
        'assay': null,
        'patient': null
     }
    ];
    const app = fixture.debugElement.componentInstance;
    app.orderStatus = 'inworkflow';
    const orderService = fixture.debugElement.injector.get(OrderService);
    spyOn(orderService, 'getInWorkflowOrdersList').and.returnValue(of(orderList));
    tick(1000);
    fixture.detectChanges();
    app.ngOnChanges();
    app.orderStatus = 'unassigned';
    spyOn(orderService, 'getUnassignedOrdersList').and.returnValue(of(orderList));
    tick(1000);
    fixture.detectChanges();
    app.ngOnChanges();
    const _routerSpy = routerSpy.navigate as jasmine.Spy;
    app.allRecords = orderList;
    app.setCurrentIndex(orderList);
    app.index = 0;
    app.findPrevNextOrders('next');
    const navArgs = _routerSpy.calls.first().args[0];
    expect (navArgs).toEqual(['orders', 'order-details', 10000073]);
  }));
});
