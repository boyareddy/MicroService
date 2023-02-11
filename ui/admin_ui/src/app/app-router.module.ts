import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddUserComponent } from './add-user/add-user.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { SettingsComponent } from './settings/settings.component';
import { Routes, RouterModule } from '@angular/router';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { LogoutComponent } from './shared/logout/logout.component';
import { AuthGuardService } from './services/auth-guard.service';
import { DeviceListComponent } from './device-list/device-list.component';
import { DeviceDetailComponent } from './device-detail/device-detail.component';
import { RegDeviceComponent } from './reg-device/reg-device.component';
import { AuditLogComponent } from './audit-log/audit-log.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { JasperReportComponent } from './jasper-report/jasper-report.component';
import { AboutComponent } from './about/about.component';
import { ViewDeviceComponent } from './view-device/view-device.component';
import { AddEditDeviceComponent } from './add-edit-device/add-edit-device.component';
import { GlobalSearchComponent } from './global-search/components/global-search/global-search.component';
const appRoute: Routes = [
    {
      path: '',
      redirectTo: '/settings',
      pathMatch: 'full'
    },
    // {
    //   path: '**',
    //   component: ResetPasswordComponent
    // },
    {
      path: 'settings',
      component: SettingsComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'add-user',
      component: AddUserComponent,
      canActivate: [AuthGuardService],
      data: {animation: 'slideBtmTop'}
    },
    {
      path: 'edit-user/:id',
      component: EditUserComponent,
      canActivate: [AuthGuardService],
      data: {animation: 'slideLftRgt'}
    },
    {
      path: 'reset-password',
      component: ResetPasswordComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'change-password',
      component: ChangePasswordComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'forgot-password',
      component: ForgotPasswordComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'my-profile/:loginName',
      component: MyProfileComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'logout',
      component: LogoutComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'device-list',
      component: DeviceListComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'device-detail/:serialNo',
      component: ViewDeviceComponent,
      canActivate: [AuthGuardService],
      data: {animation: 'slideLftRgt'}
    },
    {
      path: 'edit-device/:serialNo',
      component: AddEditDeviceComponent,
      canActivate: [AuthGuardService],
      data: {animation: 'slideLftRgt'}
    },
    {
      path: 'reg-device',
      component: AddEditDeviceComponent,
      canActivate: [AuthGuardService],
      data: {animation: 'slideBtmTop'}
    },
    {
      path: 'audit-log',
      component: AuditLogComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'report',
      component: JasperReportComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'about',
      component: AboutComponent,
      canActivate: [AuthGuardService]
    },
    {
      path: 'search',
      component: GlobalSearchComponent,
      canActivate: [AuthGuardService],
      data: {animation: 'slideLftRgt'}
    }
  ];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(
      appRoute
    ),
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
