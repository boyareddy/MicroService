import { TestBed, async, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { HttpClient } from '../../node_modules/@angular/common/http';
import { TranslateHttpLoader } from '../../node_modules/@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '../../node_modules/@ngx-translate/core';
import { HttpClientTestingModule } from '../../node_modules/@angular/common/http/testing';
import { environment } from '../environments/environment';
import { AppPropService } from './shared/app-prop.service';
import { of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('AppComponent', () => {

  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
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
        AppComponent
      ],
      providers: [
        {provide: 'UrlService', useClass: environment.apiServiceType},
        AppPropService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', async(() => {
    // tslint:disable-next-line:no-shadowed-variable
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    console.log('calling Sucess');
    expect(app).toBeTruthy();
  }));

  // it('should get the app properties Details', fakeAsync(() => {
  //   const APIProperties = {
  //       'protocol': 'http',
  //       'host': 'www.test-roche.com',
  //       'appPort': '8080',
  //     'secApi': {
  //       'module': 'security-web',
  //       'port': '90'
  //     },
  //       'rmmApi': {
  //           'module': 'rmm',
  //           'port': 86
  //       },
  //       'ammApi': {
  //           'module': 'amm',
  //           'port': 88
  //       },
  //       'ommApi': {
  //           'module': 'omm',
  //           'port': 96
  //       }
  //   };
  //   const aPIProperties = fixture.debugElement.injector.get(AppPropService);
  //   fixture.detectChanges();
  //   tick();
  //   const spy = spyOn(aPIProperties, 'getAppProperties').and.returnValue(of(APIProperties));
  //   component.ngOnInit();
  // }));

});
