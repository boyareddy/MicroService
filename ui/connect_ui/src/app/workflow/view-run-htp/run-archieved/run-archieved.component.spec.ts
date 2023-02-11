import { async, TestBed, ComponentFixture } from '@angular/core/testing';
import { RunAchievedComponent } from './run-archieved.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { RunArchievdTableComponent } from '../run-archievd-table/run-archievd-table.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { DateTimeZoneDirective } from 'src/app/shared/directives/date-time-zone.directive';
import { Location } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

export function HttpLoaderFactory(httpClient: HttpClient) {
    return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('RunArchievedComponent', () => {
    let component: RunAchievedComponent;
    let fixture: ComponentFixture<RunAchievedComponent>;
    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);
    beforeEach(async (() => {
        TestBed.configureTestingModule({
            declarations: [
                RunAchievedComponent,
                RunArchievdTableComponent,
                DateTimeZoneDirective
            ],
            imports: [
                MaterialModule,
                RouterModule,
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
                {provide: Location, useValue: routerSpy},
                {provide: Router, useValue: routerSpy},
            ]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(RunAchievedComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    // it('should create', () => {
    //     expect(component).toBeTruthy();
    // });
});
