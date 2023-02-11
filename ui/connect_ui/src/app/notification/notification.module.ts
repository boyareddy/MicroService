/**
 * Angular Core
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule} from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { NotificationRouterModule } from './notification-routing.module';
import { NotificationMessageComponent } from './notification-message/notification-message.component';
import { HTTP_INTERCEPTORS } from '../../../node_modules/@angular/common/http';
import { HttpRequestInterceptorService } from '../shared/http-request-interceptor.service';
import { CommonApiService } from '../shared/common-api.service';
import { NotificationService } from './notification.service';
import { NotificationDescriptionComponent } from './notification-description/notification-description.component';

/**
 * Order Info
 */

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    NotificationRouterModule
  ],
  declarations: [
    NotificationMessageComponent,
    NotificationDescriptionComponent
  ],
  providers: [
    NotificationService,
    CommonApiService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    }
  ]
})
export class NotificationModule { }
