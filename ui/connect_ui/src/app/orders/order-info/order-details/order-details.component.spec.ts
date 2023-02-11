import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderDetailsComponent } from './order-details.component';
import { MatCardModule, MatDividerModule } from '@angular/material';
import { Router } from '@angular/router';
import { SharedService } from '../../../shared/shared.service';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { OrderService } from '../../order.service';
import { PermissionService } from 'src/app/shared/permission.service';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { MaterialModule } from 'src/app/shared/material.module';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('OrderDetailsComponent', () => {
  let component: OrderDetailsComponent;
  let fixture: ComponentFixture<OrderDetailsComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OrderDetailsComponent,
        EmptyDataCheck,
        DateTimeZoneDirective,
      ],
      imports: [
        MatCardModule,
        MatDividerModule,
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
        OrderService,
        PermissionService,
        HttpClient,
        HttpHandler,
        { provide: Router, useValue: routerSpy },
        SharedService
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get time zone as per the given date input', () => {
    fixture = TestBed.createComponent(OrderDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.getTimeZone('1533986988374')).toEqual(undefined);
  });

  it('should navigate to the edit order page', () => {
    const _routerSpy = routerSpy.navigate as jasmine.Spy;
    fixture.detectChanges();
    fixture.componentInstance.editDetails();
    const orderStep = 0;
    // tslint:disable-next-line:radix
    expect (_routerSpy).toHaveBeenCalledWith(['orders', 'edit-order', parseInt(sessionStorage.getItem('selectedItem')), orderStep]);
    });

});
