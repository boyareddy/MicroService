import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NotificationDescriptionComponent } from './notification-description.component';
import { EmptyDataCheck } from '../../shared/pipes/emptyDataCheck.pipe';
import { DateTimeZoneDirective } from '../../shared/directives/date-time-zone.directive';
import { TranslateModule, TranslateLoader } from '../../../../node_modules/@ngx-translate/core';
import { HttpClient, HttpClientModule, HttpHandler } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MatInputModule,
  MatCardModule,
  MatToolbarModule,
  MatIconModule,
  MatMenuModule,
  MatTabsModule,
  MatSelectModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatExpansionModule,
  MatSlideToggleModule,
  MatProgressBarModule,
  MatStepperModule,
  MatRadioModule,
  MatGridListModule,
  MatTableModule,
  MatSnackBar,
  MatDividerModule} from '@angular/material';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('NotificationDescriptionComponent', () => {
  let component: NotificationDescriptionComponent;
  let fixture: ComponentFixture<NotificationDescriptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        NotificationDescriptionComponent ,
        EmptyDataCheck,
        DateTimeZoneDirective
      ],
      imports : [
        MatDividerModule,
        MatInputModule,
        MatCardModule,
        MatToolbarModule,
        MatIconModule,
        MatMenuModule,
        MatTabsModule,
        MatSelectModule,
        MatCheckboxModule,
        MatChipsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatExpansionModule,
        MatSlideToggleModule,
        MatProgressBarModule,
        MatStepperModule,
        MatRadioModule,
        MatGridListModule,
        MatTableModule,
        TranslateModule.forRoot(
          {
            loader: {
              provide: TranslateLoader,
              useFactory: HttpLoaderFactory,
              deps: [HttpClient]
            }
          }
        ),
      ],
      providers: [
        HttpClient,
        HttpHandler
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
