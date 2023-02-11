import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from 'src/app/auth/AuthService/auth-guard.service';
import { NotificationMessageComponent } from './notification-message/notification-message.component';

const notificationRoutes: Routes = [
    {
        path: '',
        component: NotificationMessageComponent,
        canActivate: [AuthGuardService]
    },
    // {
    //     path: 'create-order/bulk-upload/preview',
    //     component: BulkUploadPreviewComponent,
    //     canActivate: [AuthGuardService]
    //    // data: {animation: 'slideLftRgt'}
    // }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(notificationRoutes)
  ],
  exports: [RouterModule]
})
export class NotificationRouterModule { }
