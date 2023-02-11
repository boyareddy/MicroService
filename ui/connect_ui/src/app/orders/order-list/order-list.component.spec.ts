import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatTableModule, MatIconModule } from '@angular/material';

import { OrderListComponent } from './order-list.component';
import { OrderService } from '../order.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedService } from '../../shared/shared.service';
import { OrdersSpec } from '../../UI_SPEC/orders-spec';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { CommonApiService } from '../../shared/common-api.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { environment } from '../../../environments/environment.dev';
import { ShowCenterEllipsesComponent } from 'src/app/shared/show-center-ellipses/show-center-ellipses.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('OrderListComponent', () => {
  let component: OrderListComponent;
  let fixture: ComponentFixture<OrderListComponent>;
  let componentSpec: OrdersSpec;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
        HttpClientTestingModule,
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
      declarations: [OrderListComponent,
        ShowCenterEllipsesComponent,
        EmptyDataCheck,
        DateTimeZoneDirective
      ],
      providers: [
        OrderService,
        SharedService,
        CommonApiService,
        { provide: Router, useValue: routerSpy },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderListComponent);
    component = fixture.componentInstance;
    componentSpec = new OrdersSpec();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  /* it('table headers', () => {
    const domElement: HTMLElement = fixture.nativeElement;
    const tableHeaderDom = Array.from(domElement.querySelectorAll('th'));
    const tableHeaders: any[] = [];
    tableHeaderDom.filter(header => {
      tableHeaders.push(header.innerHTML);
    });
    expect(tableHeaders).toEqual(componentSpec.tableColumns);
  }); */

  /*  it('should get datasource from order workflow page page ', ()=> {
    // find the orderlist's DebugElement and element
      let orderlistDe = fixture.debugElement.query(By.css('order-listing'));
      let orderlistEl =orderlistDe.componentInstance;

    // mock the orderlist supplied by the parent component
    let expectedOrderList = {
      "patientSampleId": 1302,
      "accessioningId": "123456781",
      "orderComments": null,
      "orderId": 10000030,
      "patientId": 1302,
      "orderStatus": "unassigned",
      "createdDateTime": "Tue Jul 24 2018 19:10:43 GMT+0530 (India Standard Time)",
      "assayType": "NIPT",
      "sampleType":"Plasma"
    }

    component.dataSource = expectedOrderList;

    fixture.detectChanges();
  }); */

  it('table headers negetive', () => {
    const domElement: HTMLElement = fixture.nativeElement;
    const tableHeaderDom = Array.from(domElement.querySelectorAll('th'));
    const tableHeaders: any[] = [];
    tableHeaderDom.filter(header => {
      tableHeaders.push(header.innerHTML);
    });
    expect(tableHeaders).not.toEqual(['test', 'test', 'test', 'test', 'test']);
  });

  it('should get time zone as per the given date input', () => {
    fixture = TestBed.createComponent(OrderListComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.getTimeZone('1533986988374')).toEqual(undefined);
  });

  it('should show showSelectedItem by navigate to "orders/order-details"', () => {
    const _routerSpy = routerSpy.navigate as jasmine.Spy;
    const element = {
      accessioningId : 123312,
      orderId : 1235677
    };
    component.showSelectedItem(element);
    fixture.detectChanges();
    expect(_routerSpy).toHaveBeenCalledWith(['orders', 'order-details', element.orderId]);
  });

  it('should get the unassigned orders list', fakeAsync(() => {
    const expectedOrderList = [{
      'patientSampleId': 1302,
      'accessioningId': '123456781',
      'orderComments': null,
      'orderId': 10000030,
      'patientId': 1302,
      'orderStatus': 'unassigned',
      'createdDateTime': 'Tue Jul 24 2018 19:10:43 GMT+0530 (India Standard Time)',
      'assayType': 'NIPT',
      'sampleType': 'Plasma'
    }];
    const unassignedOrdersList = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    spyOn(unassignedOrdersList, 'getUnassignedOrdersList').and.returnValue(of(expectedOrderList));
  }));

});
