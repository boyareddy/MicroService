import { async, ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHandler } from '@angular/common/http';

import { OrderService } from '../../order.service';
import { ManualOrderCreationComponent } from './manual-order-creation.component';
import { HeaderComponent } from '../../../shared/header/header.component';
import { Router, ActivatedRoute } from '@angular/router';
import { SharedService } from '../../../shared/shared.service';
import { Location } from '@angular/common';
import { MinMaxDirective } from '../../../shared/directives/min-max.directive';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { of } from 'rxjs';
import { CommonApiService } from '../../../shared/common-api.service';
import { environment } from '../../../../environments/environment';
import { MaterialModule } from 'src/app/shared/material.module';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { TrimDirective } from 'src/app/shared/directives/trim-directive';
import { NotificationService } from 'src/app/notification/notification.service';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('ManualOrderCreationComponent', () => {
  let component: ManualOrderCreationComponent;
  let fixture: ComponentFixture<ManualOrderCreationComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        FormsModule, ReactiveFormsModule,
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
      declarations: [ ManualOrderCreationComponent,
        HeaderComponent,
        MinMaxDirective,
        LogoutComponent,
        TrimDirective
      ],
      providers: [
        OrderService,
        HttpClient,
        HttpHandler,
        NotificationService,
        {
          provide: Router, useValue: routerSpy
        },
        {
          provide: ActivatedRoute,
            useValue: {
              params: of({
                id: 123,
                step: 0
              })
            }
        },
        {
          provide: Location, useValue: routerSpy
        },
        SharedService,
        CommonApiService,
        {provide: 'UrlService', useClass: environment.apiServiceType}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualOrderCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should move to next', () => {
    component.nextStep();
    expect(component.step).toEqual(1);
  });

  it('should move to previous', () => {
    component.prevStep();
    expect(component.step).toEqual(-1);
  });

  it('should get the Assay type Details', fakeAsync(() => {
    fixture = TestBed.createComponent(ManualOrderCreationComponent);
    const assayTypes = [
        {
          'id': 1,
          'assayName': 'NIPT'
        }
    ];
    const app = fixture.debugElement.componentInstance;
    const getAssayDetails = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(getAssayDetails, 'getAssayTypes').and.returnValue(of(assayTypes));
    app.ngOnInit();
  }));

  it('should get the EggDonner Details', fakeAsync(() => {
    const EggDonner = [
      {
        'listType': 'egg donor',
        'value': 'No'
    }
    ];
    const eggDonner = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(eggDonner, 'getEggDonner').and.returnValue(of(EggDonner));
    component.ngOnInit();
  }));

  it('should get the IVFStatus Details', fakeAsync(() => {
    const IVFStatusValue = [
      {
        'listType': 'ivf status',
        'value': 'Yes'
      }
    ];
    const IVFStatus = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(IVFStatus, 'getIVFStatus').and.returnValue(of(IVFStatusValue));
    component.ngOnInit();
  }));

  it('should get the Fetuses Details', fakeAsync(() => {
    const FetusesValues = [
      {
        'listType': 'ivf status',
        'value': 'Yes'
      }
    ];
    const fetusesDetails = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(fetusesDetails, 'getNumberOfFetuses').and.returnValue(of(FetusesValues));
    component.ngOnInit();
  }));

  it('should get the MaternalAge Details', fakeAsync(() => {
    const MaternalAgeValues = [
      {
        'fieldName': 'Maternal Age',
        'minVal': 1,
        'maxVal': 99,
        'expression': 'NA'
    }
    ];
    const MaternalAge = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(MaternalAge, 'getMaternalAge').and.returnValue(of(MaternalAgeValues));
    component.ngOnInit();
  }));

  it('should get the TestOptions Details', fakeAsync(() => {
    const TestOptionsValues = [
      {
        'testName': 'Harmony',
        'testProtocol': 'Cfna ss 2000',
        'testDescription': 'Harmony',
        'sequenceId': 1
        }
    ];
    const TestOptions = fixture.debugElement.injector.get(OrderService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(TestOptions, 'getTestOptions').and.returnValue(of(TestOptionsValues));
    component.ngOnInit();
  }));
});
