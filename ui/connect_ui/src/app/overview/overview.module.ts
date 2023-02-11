import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OverviewRoutingModule } from './overview-routing.module';
import { LobbyComponent } from './lobby/lobby.component';
import { IconComponent } from './icon/icon.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptorService } from '../shared/http-request-interceptor.service';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    OverviewRoutingModule,
    SharedModule
  ],
  declarations: [
    LobbyComponent,
    IconComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    }
  ]
})
export class OverviewModule { }
