import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';

import { AppComponent } from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { SharedModule } from './shared/shared.module';
import { AppRoutingModule } from './app-routing.module';
import { HttpRequestInterceptorService } from './shared/http-request-interceptor.service';
import { AuthGuardService } from './auth/AuthService/auth-guard.service';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { VersionComponent } from './version/version.component';
import { environment } from '../environments/environment';
import { SharedService } from './shared/shared.service';
import { UserIdleModule } from 'angular-user-idle';
import { IdealService } from './shared/ideal.service';
import { GlobalSearchModule } from './global-search/global-search.module';
import { PermissionService } from './shared/permission.service';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, '/ui-conf/i18n/lang.', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    VersionComponent
  ],
  imports: [
    UserIdleModule,
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    GlobalSearchModule,
    BrowserAnimationsModule,
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
    AuthGuardService,
    SharedService,
    PermissionService,
    IdealService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    },
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    {provide: 'UrlService', useClass: environment.apiServiceType}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
