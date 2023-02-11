import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewContainerMappingComponent } from './view-container-mapping.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { Router } from '@angular/router';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { WorkflowService } from '../workflow.service';
import { SharedService } from 'src/app/shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { MappedContainerComponent } from '../mapped-container/mapped-container.component';
import { ContainerMappingFooterComponent } from '../container-mapping-footer/container-mapping-footer.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { environment } from 'src/environments/environment';

import { Location } from '@angular/common';
import { NotificationService } from 'src/app/notification/notification.service';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('ViewContainerMappingComponent', () => {
  let component: ViewContainerMappingComponent;
  let fixture: ComponentFixture<ViewContainerMappingComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ViewContainerMappingComponent,
        HeaderComponent,
        MappedContainerComponent,
        ContainerMappingFooterComponent,
        LogoutComponent,
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
      ],
      providers: [
        WorkflowService,
        SharedService,
        CommonApiService,
        NotificationService,
        {provide: 'UrlService', useClass: environment.apiServiceType},
        { provide: Router, useValue: routerSpy },
        { provide: Location, useValue: routerSpy }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewContainerMappingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
