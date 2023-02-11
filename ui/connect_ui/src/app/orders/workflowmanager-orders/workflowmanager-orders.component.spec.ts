import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';

import { WorkflowmanagerOrdersComponent } from './workflowmanager-orders.component';
import { HeaderComponent } from '../../shared/header/header.component';
import {
  MatSnackBar
} from '@angular/material';
import { OrdersUnassignedComponent } from '../orders-unassigned/orders-unassigned.component';
import { OrdersInworkflowComponent } from '../orders-inworkflow/orders-inworkflow.component';
import { OrderListComponent } from '../order-list/order-list.component';
import { OrderService } from '../order.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SharedService } from '../../shared/shared.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from 'node_modules/@angular/common/http';
import { TranslateHttpLoader } from 'node_modules/@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from 'node_modules/@ngx-translate/core';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from '../../../environments/environment';
import { MaterialModule } from 'src/app/shared/material.module';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { ResponseProgressComponent } from 'src/app/shared/response-progress/response-progress.component';
import { ShowMoreComponent } from 'src/app/shared/show-more/show-more.component';
import { FlagPopupComponent } from 'src/app/shared/flag-popup/flag-popup.component';
import { InlineEditComponent } from 'src/app/shared/inline-edit/inline-edit.component';
import { FormsModule } from '@angular/forms';
import { ShowCenterEllipsesComponent } from 'src/app/shared/show-center-ellipses/show-center-ellipses.component';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { NotificationService } from 'src/app/notification/notification.service';
import { CustomSnackbarComponent } from 'src/app/shared/custom-snackbar/custom-snackbar.component';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('WorkflowmanagerOrdersComponent', () => {
  let component: WorkflowmanagerOrdersComponent;
  let fixture: ComponentFixture<WorkflowmanagerOrdersComponent>;
  let snackBar: MatSnackBar;
  let sharedService: SharedService;
  const routerSpy = jasmine.createSpyObj('Router', ['naviagateByUrl']);
  const orderService = jasmine.createSpyObj('OrderService', ['getUnassignedOrdersList']);

 beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WorkflowmanagerOrdersComponent,
        HeaderComponent,
        OrdersUnassignedComponent,
        OrdersInworkflowComponent,
        OrderListComponent,
        LogoutComponent,
        ResponseProgressComponent,
        ShowMoreComponent,
        FlagPopupComponent,
        InlineEditComponent,
        ShowCenterEllipsesComponent,
        EmptyDataCheck,
        DateTimeZoneDirective,
        CustomSnackbarComponent
      ],
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        FormsModule,
        MaterialModule,
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
        {provide: OrderService, useValue:  orderService},
        SharedService,
        OrderService,
        CommonApiService,
        NotificationService,
        { provide: Location, useValue: routerSpy },
        { provide: Router, useValue: routerSpy },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
      .compileComponents();

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowmanagerOrdersComponent);
    component = fixture.componentInstance;
    sharedService = fixture.debugElement.injector.get(SharedService);
    fixture.detectChanges();
  });

  beforeEach(inject([MatSnackBar],
    (sb: MatSnackBar) => {
      snackBar = sb;
    }));

  afterEach(() => {
    // component.orderCount[0];
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should return value with unassigned header tag name', () => {
  //   component.orderCount[0].unassigned = 2;

  //   // tslint:disable-next-line:no-unused-expression
  //   component.orderCount[0].unassigned > 0;

  //   expect(component.getUnassignedOrders()).toBe(`Unassigned (${component.orderCount[0].unassigned})`);
  // });

  // it('should return only unassinged header tag name without count', () => {
  //   component.orderCount[0].unassigned = 0;

  //   expect(component.getUnassignedOrders()).toBe('Unassigned');
  // });

  // it('should return value with In workflow header tag name', () => {
  //   component.orderCount[0].inworkflow = 2;

  //   expect(component.getInworkflowOrders()).toBe(`In workflow [ ${component.orderCount[0].inworkflow} ]`);
  // });

  // it('should return only Pending release header tag name without count', () => {
  //   component.orderCount[0].inworkflow = 0;

  //   expect(component.getInworkflowOrders()).toBe('In workflow');
  // });

  // it('should return value with Pending release header tag name', () => {
  //   component.orderCount[0].pendingrelease = 2;

  //   expect(component.getPendingRelease()).toBe(`Pending release [ ${component.orderCount[0].pendingrelease} ]`);
  // });


  // it('should return only Pending release header tag name without count', () => {
  //   component.orderCount[0].pendingrelease = 0;

  //   expect(component.getPendingRelease()).toBe('Pending release');
  // });

  // it('should return only Completed header tag name without count', () => {
  //   component.orderCount[0].completed = 10;

  //   expect(component.getCompleted()).toBe('Completed');
  // });

  //  it('should enables the snackbar when loaded component', () => {
  //   sharedService.setData('orderCreated', 'order successfully created');

  //   fixture.detectChanges();

  //   component.enableSnackBar();

  //   sharedService.getData('orderCreated');

  //   expect(component.enableSnackBar()).toBeUndefined();
  // });

 /*  it('should get the unassigned orders from the service layer', () => {
    orderService.getUnassignedOrdersList.and.returnValue(of({}));
    component.unassignedOrders();
  }); */
});
