import { SharedService } from '../../shared/shared.service';
import { Location, LocationStrategy } from '@angular/common';
import { OrderService } from '../order.service';
import { AuthService } from '../../auth/AuthService/auth.service';
import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
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
  MatSnackBar,
  MatSpinner
} from '@angular/material';
import { OrderListComponent } from '../order-list/order-list.component';
import { OrdersUnassignedComponent } from './orders-unassigned.component';
import { HeaderComponent } from '../../shared/header/header.component';
import { OrdersSpec } from '../../UI_SPEC/orders-spec';
import { MdePopoverModule } from '@material-extended/mde';
import { TranslateHttpLoader } from '../../../../node_modules/@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '../../../../node_modules/@ngx-translate/core';
import { RouterTestingModule } from '../../../../node_modules/@angular/router/testing';
import { CommonApiService } from '../../shared/common-api.service';
import { environment } from '../../../environments/environment';
import { ResponseProgressComponent } from 'src/app/shared/response-progress/response-progress.component';
import { LogoutComponent } from '../../shared/logout/logout.component';
import { ShowCenterEllipsesComponent } from 'src/app/shared/show-center-ellipses/show-center-ellipses.component';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { MaterialModule } from '../../shared/material.module';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('OrdersUnassignedComponent', () => {
  let component: OrdersUnassignedComponent;
  let fixture: ComponentFixture<OrdersUnassignedComponent>;
  let componentSpec: OrdersSpec;

  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  const locStrategy = jasmine.createSpyObj('LocationStrategy', ['getBaseHref']);
  const sharedService = jasmine.createSpyObj('SharedService', ['getData']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, MatButtonModule,
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
        MdePopoverModule,
        MaterialModule,
        RouterTestingModule.withRoutes([]),
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
      declarations: [
        OrdersUnassignedComponent,
        HeaderComponent,
        OrderListComponent,
        ResponseProgressComponent,
        LogoutComponent,
        ShowCenterEllipsesComponent,
        EmptyDataCheck,
        DateTimeZoneDirective,
       ],
      providers: [
        AuthService,
        HttpClient,
        HttpHandler,
        OrderService,
        CommonApiService,
        MatSnackBar,
        {provide: SharedService, useValue: sharedService},
        Location,
        { provide: LocationStrategy, useValue: locStrategy },
        { provide: Router, useValue: routerSpy },
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdersUnassignedComponent);
    component = fixture.componentInstance;
    componentSpec = new OrdersSpec();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('unassigned table header name should be "Unassigned orders"', () => {
  //   const domElement: HTMLElement = fixture.nativeElement;

  //   const tableName = domElement.querySelector('span');

  //   expect(tableName.innerText).toBe(componentSpec.tableName);
  // });

  // it('unassigned table header name should not be other than "Unassigned orders"', () => {
  //   const domElement: HTMLElement = fixture.nativeElement;
  //   const tableName = domElement.querySelector('span');
  //   expect(tableName.innerHTML).not.toBe('test');
  // });

  it('unassigned table should have four header titles', () => {
    const domElement: HTMLElement = fixture.nativeElement;
    const tableHeaders = domElement.querySelectorAll('mat-tab-body > table');
    expect(tableHeaders.length).not.toBe(componentSpec.tableColumnLength);
  });

  // it('button name should be "Create Order"', () => {
  //   const domElement: HTMLElement = fixture.nativeElement;
  //   const createBtn = domElement.querySelector('button > span');
  //   expect(createBtn.innerHTML).toBe(componentSpec.createBtnName);
  // });

  /**
   * table header with names checking
   */
  // +

  // describe('goToCreateOrder method', () => {
  //   it('should navigate to the create order page', () => {
  //   const _routerSpy = routerSpy.navigate as jasmine.Spy;
  //   fixture.detectChanges();
  //   fixture.componentInstance.goToCreateOrder();
  //   expect (_routerSpy).toHaveBeenCalledWith(['orders', 'create-order']);
  //   });
  // });

});
