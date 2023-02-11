import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SampleListComponent } from './sample-list.component';
import { MaterialModule } from 'src/app/shared/material.module';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ShowMoreComponent } from 'src/app/shared/show-more/show-more.component';
import { InlineEditComponent } from 'src/app/shared/inline-edit/inline-edit.component';
import { FlagPopupComponent } from 'src/app/shared/flag-popup/flag-popup.component';
import { FormsModule } from '@angular/forms';

export function HttpLoaderFactory(httpClient: HttpClient) {
    return new TranslateHttpLoader(httpClient, 'assets/i18n/lang.', '.json');
}

describe('SampleListComponent', () => {
    let component: SampleListComponent;
    let fixeture: ComponentFixture<SampleListComponent>;
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                SampleListComponent,
                ShowMoreComponent,
                InlineEditComponent,
                FlagPopupComponent,
            ],
            imports: [
                MaterialModule,
                FormsModule,
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
            providers: []
        }).compileComponents();
    }));

    beforeEach(() => {
        fixeture = TestBed.createComponent(SampleListComponent);
        component = fixeture.componentInstance;
        fixeture.detectChanges();
    });

    // it('should create', () => {
    //     expect(component).toBeTruthy();
    // });
});
