import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';

import { NewCardComponent } from './new-card.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MaterialModule } from 'src/app/shared/material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { Router } from '@angular/router';
import { FlagService } from 'src/app/shared/flag-popup/flag.service';
import { RouterTestingModule } from '@angular/router/testing';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('NewCardComponent', () => {
  let component: NewCardComponent;
  let fixture: ComponentFixture<NewCardComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NewCardComponent, PluralCheckPipe],
      imports: [
        BrowserAnimationsModule,
         MaterialModule,
         HttpClientModule,
         RouterTestingModule,
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
        { provide: Router, useValue: routerSpy },
        FlagService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCardComponent);
    component = fixture.componentInstance;
    component.runCardInfo = {flags: 'AA'};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate the card to rundetails based on runresult ID', () => {
    const cardInfo = {
      'runResultId': 153,
      'deviceId': 'MP001-12',
      'processStepName': 'LP Post PCR/Pooling',
      'deviceRunId': '12221',
      'runStatus': 'Completed with flags',
      'wfmsflag': '',
      'dvcRunResult': null,
      'runFlag': null,
      'operatorName': 'chan',
      'comments': 'comment',
      'totalSamplecount': '10',
      'totalSampleFailedCount': '1 sample failed',
      'totalSampleFlagCount': '3 samples flagged',
      'inputcontainerIds': [
        '11111',
        '8tubeid_222'
      ],
      'outputcontainerIds': [
        {
          'stripId': '1234',
          'samplecounts': '2'
        }
      ]
    };
    const navigate = component.getRunID(cardInfo);
    expect(navigate).toBeUndefined();
  });


});

class RouterStub {
  navigateByUrl(url: string) { return url; }
}
