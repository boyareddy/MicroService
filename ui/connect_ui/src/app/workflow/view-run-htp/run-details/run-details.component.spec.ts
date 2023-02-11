import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RunDetailsComponent } from './run-details.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from 'src/app/shared/material.module';
import { PluralCheckPipe } from 'src/app/shared/pipes/plural-check.pipe';
import { OngoingProgressbarComponent } from './ongoing-progressbar/ongoing-progressbar.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { SampleListComponent } from './sample-list/sample-list.component';
import { SampleRunCardComponent } from './sample-run-card/sample-run-card.component';
import { ResponseProgressComponent } from 'src/app/shared/response-progress/response-progress.component';
import { LogoutComponent } from 'src/app/shared/logout/logout.component';
import { WorkflowService } from '../../workflow.service';
import { SharedService } from 'src/app/shared/shared.service';
import { CommonApiService } from 'src/app/shared/common-api.service';
import { NotificationService } from 'src/app/notification/notification.service';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';
import { ShowMoreComponent } from 'src/app/shared/show-more/show-more.component';
import { InlineEditComponent } from 'src/app/shared/inline-edit/inline-edit.component';
import { FlagPopupComponent } from 'src/app/shared/flag-popup/flag-popup.component';
import { SingleRowDetailsComponent } from './single-row-details/single-row-details.component';
import { FormsModule } from '@angular/forms';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { Location } from '@angular/common';
import { of } from 'rxjs';

export function HttpLoaderFactory(httpClient: HttpClient) {
    return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('RunDetailsComponent', () => {
    let component: RunDetailsComponent;
    let fixture: ComponentFixture<RunDetailsComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                RunDetailsComponent,
                HeaderComponent,
                PluralCheckPipe,
                OngoingProgressbarComponent,
                SampleListComponent,
                SampleRunCardComponent,
                ResponseProgressComponent,
                LogoutComponent,
                ShowMoreComponent,
                InlineEditComponent,
                FlagPopupComponent,
                SingleRowDetailsComponent,
                DateTimeZoneDirective
            ],
            imports: [
                BrowserAnimationsModule,
                MaterialModule,
                FormsModule,
                HttpClientModule,
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
                {
                    provide: ActivatedRoute,
                      useValue: {
                        params: of({
                            runId: '1111',
                        })
                      }
                  },
                {
                  provide: Router, useValue: routerSpy
                },
                {provide: 'UrlService', useClass: environment.apiServiceType},
                {
                  provide: Location, useValue: routerSpy
                }
               ]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(RunDetailsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    // it('should create', () => {
    //     expect(component).toBeTruthy();
    // });
});
