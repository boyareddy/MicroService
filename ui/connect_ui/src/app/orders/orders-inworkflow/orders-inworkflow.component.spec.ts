import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { OrdersInworkflowComponent } from './orders-inworkflow.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { OrderService } from '../order.service';
import { Observable, of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatInputModule,
  MatCardModule,
  MatToolbarModule,
  MatIconModule,
  MatMenuModule,
  MatTabsModule,
  MatSelectModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatExpansionModule,
  MatSlideToggleModule,
  MatProgressBarModule,
  MatStepperModule,
  MatRadioModule,
  MatGridListModule,
  MatTableModule,
  MatSnackBar
} from '@angular/material';
import { AuthService } from '../../auth/AuthService/auth.service';
import { SharedService } from '../../shared/shared.service';
import { LocationStrategy } from '@angular/common';
import { Router } from '@angular/router';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from '../../../environments/environment';
import { ShowMoreComponent } from '../../shared/show-more/show-more.component';
import { FlagPopupComponent } from '../../shared/flag-popup/flag-popup.component';
import { MaterialModule } from '../../shared/material.module';
import { InlineEditComponent } from '../../shared/inline-edit/inline-edit.component';
import { ResponseProgressComponent } from '../../shared/response-progress/response-progress.component';
import { FormsModule } from '../../../../node_modules/@angular/forms';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('OrdersInworkflowComponent', () => {
  let component: OrdersInworkflowComponent;
  let fixture: ComponentFixture<OrdersInworkflowComponent>;

  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  const locStrategy = jasmine.createSpyObj('LocationStrategy', ['getBaseHref']);
  const sharedService = jasmine.createSpyObj('SharedService', ['getData']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OrdersInworkflowComponent,
        ShowMoreComponent,
        FlagPopupComponent,
        InlineEditComponent,
        ResponseProgressComponent
      ],
      imports: [BrowserAnimationsModule,
          MaterialModule,
          FormsModule,
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
        AuthService,
        HttpClient,
        HttpHandler,
        OrderService,
        CommonApiService,
        SharedService,
        MatSnackBar,
        Location,
        { provide: LocationStrategy, useValue: locStrategy },
        { provide: Router, useValue: routerSpy },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdersInworkflowComponent);
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
   const orderList = [
     {
       'accessioningId': '123456782',
       'orderComments': null,
       'orderId': 10000031,
       'workflowType': 'LP Pre-PCR',
       'assaytype': 'NIPT',
       'sampletype': 'Plasma',
       'flags': 'F1',
       'workflowStatus': 'completed'
     },
  ];
   const app = fixture.debugElement.componentInstance;
   const orderService = fixture.debugElement.injector.get(OrderService);
   fixture.detectChanges();
   tick();
   const spy = spyOn(orderService, 'getInWorkflowOrdersList').and.returnValue(of(orderList));
   app.ngOnInit();
 }));

 it('should show showSelectedItem by navigate to "orders/order-details"', fakeAsync(() => {
  const _routerSpy = routerSpy.navigate as jasmine.Spy;
  const element = {
    accessioningId : 123312,
    orderId : 1235677
  };
  // tslint:disable-next-line:no-shadowed-variable
  const sharedService = fixture.debugElement.injector.get(SharedService);
   const spy = spyOn(sharedService, 'setData').and.callThrough();
   tick();
   component.showSelectedItem(element);
  fixture.detectChanges();
  expect(_routerSpy).toHaveBeenCalledWith(['orders', 'order-details', element.orderId]);
}));

it('should navigate to the edit order page', () => {
  const _routerSpy = routerSpy.navigate as jasmine.Spy;
  fixture.detectChanges();
  // fixture.componentInstance.showSelectedItem(10000);
  const ele = {
    orderId: 10000
  };
  component.showSelectedItem(ele);
  // tslint:disable-next-line:radix
  expect (_routerSpy).toHaveBeenCalledWith(['orders', 'order-details', 10000]);
  });

  it('should call showselected item', () => {
    expect(component.isEmptyFlag(null)).toBeTruthy();
  });

  it('should call showselected item', () => {
    expect(component.isEmptyFlag('')).toBeTruthy();
  });
  it('should call showselected item', () => {
    expect(component.isEmptyFlag('aa')).toBeFalsy();
  });

  // it('should pass the API call for update', fakeAsync(() => {
  //   const el = {
  //       'accessioningId': '123456782',
  //       'workflowType': 'Sequencing'
  //     };
  //   const app = fixture.debugElement.componentInstance;
  //   const orderService = fixture.debugElement.injector.get(OrderService);
  //   component.update(el, 'pass');
  //   const event = {
  //     accesssioningId: el.accessioningId,
  //     comments: 'pass',
  //     processStepName: el.workflowType
  //   };

  //   const spy = spyOn(orderService, 'getInWorkflowOrdersList').and.returnValue(of(orderList));
  //   app.ngOnInit();
  // }));
});




