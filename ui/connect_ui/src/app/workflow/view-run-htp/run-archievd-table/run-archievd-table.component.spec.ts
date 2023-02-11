import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunArchievdTableComponent } from './run-archievd-table.component';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { MaterialModule } from 'src/app/shared/material.module';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}


describe('RunArchievdTableComponent', () => {
  let component: RunArchievdTableComponent;
  let fixture: ComponentFixture<RunArchievdTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        RunArchievdTableComponent,
        DateTimeZoneDirective,
        HeaderComponent,
        LogoutComponent
       ],
      imports: [
        BrowserAnimationsModule,
        MaterialModule,
        RouterTestingModule,
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
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunArchievdTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
