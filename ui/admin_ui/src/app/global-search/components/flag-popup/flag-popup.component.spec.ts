import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FlagPopupComponent } from './flag-popup.component';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { SharedService } from '../shared.service';
import { MdePopoverModule } from '@material-extended/mde';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('FlagPopupComponent', () => {
  let component: FlagPopupComponent;
  let fixture: ComponentFixture<FlagPopupComponent>;
  let translate;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlagPopupComponent ],
      imports: [
        HttpClientTestingModule,
        MdePopoverModule,
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
        SharedService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlagPopupComponent);
    component = fixture.componentInstance;
    translate = fixture.debugElement.injector.get(TranslateService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create getFormattedFlags', () => {
    const sharedService = fixture.debugElement.injector.get(SharedService);
    const sample = {'assaytype': 'NIPTHTP', 'processStepName': 'NA Extraction', 'workflowType': ''};
    component.sampleInput = sample;
    translate.setTranslation('en', { MENU_TITLE: 'reporting' });
    translate.use('en');
    fixture.detectChanges();
    const flags = {
      'lp24': {
          'F1': 'Fatal',
          'F2': 'Fatal',
          'F3': 'Fatal',
          'F4': 'Fatal',
          'F5': 'Fatal',
          'F6': 'Fatal',
          'F7': 'Critical',
          'F8': 'Fatal',
          'F9': 'Critical',
          'F10': 'Fatal',
          'F12': 'Fatal',
          'F13': 'Fatal',
          'F14': 'Fatal',
          'F15': 'Fatal',
          'F17': 'Critical',
          'F18': 'Critical',
          'F19': 'Critical',
          'F20': 'Critical',
          'F21': 'Critical',
          'F22': 'Critical',
          'F23': 'Fatal',
          'F24': 'Information',
          'F25': 'Fatal',
          'F26': 'Fatal',
          'F27': 'Fatal',
          'F28': 'Critical',
          'F29': 'Fatal',
          'F30': 'Fatal',
          'F32': 'Information',
          'F33': 'Information',
          'F34': 'Information',
          'F35': 'Information',
          'F36': 'Fatal',
          'F37': 'Fatal',
          'F38': 'Information',
          'F39': 'Critical',
          'F40': 'Information',
          'F41': 'Fatal',
          'F42': 'Critical'
      }
  };
    sharedService.setData('flags', flags);
    const frFlag = component.getFormattedFlags('F1');
    expect(frFlag).toEqual([{ flagCode: 'F1', flagDescription: null, flagIcon: 'fatal' }]);
  });
});
