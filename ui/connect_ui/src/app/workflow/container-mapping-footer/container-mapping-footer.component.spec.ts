import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerMappingFooterComponent } from './container-mapping-footer.component';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { MaterialModule } from 'src/app/shared/material.module';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { HttpClientTestingModule } from '@angular/common/http/testing';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('ContainerMappingFooterComponent', () => {
  let component: ContainerMappingFooterComponent;
  let fixture: ComponentFixture<ContainerMappingFooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ContainerMappingFooterComponent,
        PluralCheckPipe
      ],
      imports: [
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
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerMappingFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
