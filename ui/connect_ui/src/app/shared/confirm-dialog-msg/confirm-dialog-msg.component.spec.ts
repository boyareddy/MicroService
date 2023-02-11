import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDialogMsgComponent } from './confirm-dialog-msg.component';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { StringConstants } from 'src/app/standard-names/constants';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

export class TranslateServiceStub {

  public get(key: any): any {
    of(key);
  }
}

describe('ConfirmDialogMsgComponent', () => {
  let component: ConfirmDialogMsgComponent;
  let fixture: ComponentFixture<ConfirmDialogMsgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmDialogMsgComponent ],
      imports: [
        MatDialogModule,
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
      ],
      providers: [
        { provide:  MatDialogRef,  userValue:  {data: StringConstants.STRING('CANCEL_UPDATE_ORDER')} },
        { provide:  MAT_DIALOG_DATA, useValue: {} },
        { provide: TranslateService, useClass: TranslateServiceStub}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmDialogMsgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
