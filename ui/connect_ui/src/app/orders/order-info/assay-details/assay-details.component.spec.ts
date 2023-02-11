import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssayDetailsComponent } from './assay-details.component';
import { MatCardModule, MatDividerModule, MatChipsModule } from '@angular/material';
import { Router } from '@angular/router';
import { SharedService } from '../../../shared/shared.service';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { OrderService } from '../../order.service';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { MaterialModule } from 'src/app/shared/material.module';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('AssayDetailsComponent', () => {
  let component: AssayDetailsComponent;
  let fixture: ComponentFixture<AssayDetailsComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AssayDetailsComponent,
        EmptyDataCheck,
      ],
      imports: [MatCardModule,
                MatDividerModule,
                MatChipsModule,
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
        HttpClient,
        HttpHandler,
        {
          provide: Router, useValue: routerSpy
        },
        {
          provide: Location, useValue: routerSpy
        },
        SharedService
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssayDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to the edit order page', () => {
    const _routerSpy = routerSpy.navigate as jasmine.Spy;
    fixture.detectChanges();
    fixture.componentInstance.editDetails();
    const orderStep = 1;
    // tslint:disable-next-line:radix
    expect (_routerSpy).toHaveBeenCalledWith(['orders', 'edit-order', parseInt(sessionStorage.getItem('selectedItem')), orderStep]);
    });

});
