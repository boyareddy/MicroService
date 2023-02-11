import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ContainerMappedSamplesComponent } from './container-mapped-samples.component';
import { HeaderComponent } from '../../shared/header/header.component';
import { WorkflowService } from '../workflow.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, Subject } from 'rxjs';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LogoutComponent } from '../../shared/logout/logout.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SharedAuthService } from '../../shared/shared-auth.service';
import { environment } from '../../../environments/environment';
import { CommonApiService } from '../../shared/common-api.service';
import { Location } from '@angular/common';
import { MaterialModule } from 'src/app/shared/material.module';
import { NotificationService } from 'src/app/notification/notification.service';

const mockRoute: any = { snapshot: {}};

mockRoute.parent = { params: new Subject<any>()};
mockRoute.params = new Subject<any>();
mockRoute.queryParams = new Subject<any>();

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('ContainerMappedSamplesComponent', () => {
  let component: ContainerMappedSamplesComponent;
  let fixture: ComponentFixture<ContainerMappedSamplesComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  const mock = {
    parent: {
      params: of({})
    }
  };


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ContainerMappedSamplesComponent,
        HeaderComponent,
        LogoutComponent
      ],
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
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
      providers: [
        WorkflowService,
        SharedAuthService,
        CommonApiService,
        NotificationService,
        { provide: Location, useValue: routerSpy },
        { provide: Router, useValue: routerSpy },
        {provide: 'UrlService', useClass: environment.apiServiceType},
        { provide: ActivatedRoute, useValue: mockRoute },
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerMappedSamplesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get the container id and plate id', () => {
    component.plateNo = '';
    component.containerId = '';
    expect(component.ngOnInit()).toBeUndefined();
  });

  it('should call the getContainerMappedSamples', fakeAsync(() => {
    const mappedSamples =  [
      {
        'accessioningID': '11113',
        'containerType': 'containertype',
        'assayType': 'NIPT',
        'position': 'p2',
        'containerID': 'A1'
      }
    ];
    const MappedSamples = fixture.debugElement.injector.get(WorkflowService);
    fixture.detectChanges();
    tick();
    const spy = spyOn(MappedSamples, 'getContainerMappedSamples').and.returnValue(of(mappedSamples));
    component.ngOnInit();
  }));
});
