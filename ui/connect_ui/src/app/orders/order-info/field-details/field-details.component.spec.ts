import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldDetailsComponent } from './field-details.component';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { DateTimeZoneDirective } from '../../../shared/directives/date-time-zone.directive';
import { HttpClientTestingModule } from '../../../../../node_modules/@angular/common/http/testing';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('FieldDetailsComponent', () => {
  let component: FieldDetailsComponent;
  let fixture: ComponentFixture<FieldDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FieldDetailsComponent,
        DateTimeZoneDirective,
       ],
      imports: [
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
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
