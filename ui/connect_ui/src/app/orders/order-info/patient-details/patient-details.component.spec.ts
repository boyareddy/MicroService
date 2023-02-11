import { async, ComponentFixture, TestBed, fakeAsync } from '@angular/core/testing';

import { PatientDetailsComponent } from './patient-details.component';
import { MatDividerModule, MatCardModule, MatTooltipModule } from '@angular/material';
import { Router } from '@angular/router';
import { SharedService } from '../../../shared/shared.service';
import { SpyLocation } from '@angular/common/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { ShowMoreComponent } from '../../../shared/show-more/show-more.component';
import { PermissionService } from 'src/app/shared/permission.service';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { EmptyDataCheck } from 'src/app/shared/pipes/emptyDataCheck.pipe';
import { TooltipModule } from '../../../../../node_modules/ng2-tooltip-directive';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('PatientInfoComponent', () => {
  let component: PatientDetailsComponent;
  let fixture: ComponentFixture<PatientDetailsComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  let location: Location;
  let router: Router;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PatientDetailsComponent,
        ShowMoreComponent,
        PluralCheckPipe,
        EmptyDataCheck,
      ],
      imports: [
        MatCardModule,
        MatDividerModule,
        RouterTestingModule,
        MatTooltipModule,
        TooltipModule,
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
        SharedService,
        PermissionService,
        HttpClient,
        HttpHandler,
        SpyLocation,
        { provide: Location, useValue: routerSpy },
        { provide: Router, useValue: routerSpy },
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    location = TestBed.get(Location);
    router = TestBed.get(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to the edit order page', () => {
    const _routerSpy = routerSpy.navigate as jasmine.Spy;
    fixture.detectChanges();
    fixture.componentInstance.editDetails();
    const orderStep = 2;
    // tslint:disable-next-line:radix
    expect (_routerSpy).toHaveBeenCalledWith(['orders', 'edit-order', parseInt(sessionStorage.getItem('selectedItem')), orderStep]);
    });
});
