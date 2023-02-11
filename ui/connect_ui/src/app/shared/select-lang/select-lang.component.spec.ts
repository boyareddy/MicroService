import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectLangComponent } from './select-lang.component';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}


describe('SelectLangComponent', () => {
  let component: SelectLangComponent;
  let fixture: ComponentFixture<SelectLangComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectLangComponent ],
      imports : [FormsModule,
        HttpClientTestingModule,
        TranslateModule.forRoot(
        {
          loader: {
            provide: TranslateLoader,
            useFactory: HttpLoaderFactory,
            deps: [HttpClient]
          }
        }
      )]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectLangComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should change the language on change', () => {
    const lang = 'fr';
    expect(component.onLangChange(lang)).toBe(undefined);
  });
});
