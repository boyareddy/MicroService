import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileuploadErrorsComponent } from './fileupload-errors.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClientTestingModule } from '@angular/common/http/testing';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('FileuploadErrorsComponent', () => {
  let component: FileuploadErrorsComponent;
  let fixture: ComponentFixture<FileuploadErrorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileuploadErrorsComponent ],
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
    fixture = TestBed.createComponent(FileuploadErrorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
