import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SingleRowDetailsComponent } from './single-row-details.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('SingleRowDetailsComponent', () => {
  let component: SingleRowDetailsComponent;
  let fixture: ComponentFixture<SingleRowDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleRowDetailsComponent, DateTimeZoneDirective ],
      imports: [
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
    fixture = TestBed.createComponent(SingleRowDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
