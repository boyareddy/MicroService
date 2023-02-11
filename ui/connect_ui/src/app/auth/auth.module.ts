import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';

/* Services */
import { AuthService } from './AuthService/auth.service';
import { SharedModule } from '../shared/shared.module';
import { AuthRouterModule } from './auth-router.module';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptorService } from '../shared/http-request-interceptor.service';
import { AuthGuardService } from './AuthService/auth-guard.service';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    AuthRouterModule,
    ReactiveFormsModule
  ],
  declarations: [
    LoginComponent
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    }
  ]
})
export class AuthModule {

}
