import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { CommonModule, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule, FormsModule  } from '@angular/forms';

import { AppComponent } from './app.component';
import { AddUserComponent } from './add-user/add-user.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { UserListComponent } from './user-list/user-list.component';
import {
    MatButtonModule,
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
    MatSnackBarModule,
    MatDialogModule,
    MatSortModule,
    MatTooltipModule,
    MatProgressSpinnerModule
} from '@angular/material';
import { AdminUiHeaderComponent } from './admin-ui-header/admin-ui-header.component';
import { UserTableComponent } from './user-table/user-table.component';
import { SettingsComponent } from './settings/settings.component';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Routes, RouterModule } from '@angular/router';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { MdePopoverModule } from '@material-extended/mde';
import { CustomSnackBarComponent } from './custom-snack-bar/custom-snack-bar.component';
import { MinMaxDirective } from './directives/min-max.directive';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { ShowMoreComponent } from './shared/show-more/show-more.component';
import { SelectLangComponent } from './shared/select-lang/select-lang.component';
import { LogoutComponent } from './shared/logout/logout.component';
import { TrimDirective } from './directives/trim-directive';
import { XhrInterceptorService } from './services/xhr-interceptor.service';
import { AppPropService } from './services/app-prop.service';
import { IdealService } from './services/ideal.service';
import { AppRoutingModule } from './app-router.module';
import { AuthGuardService } from './services/auth-guard.service';
import { SharedAuthService } from './services/shared-auth.service';
import { DeviceListComponent } from './device-list/device-list.component';
import { DeviceComponent } from './device/device.component';
import { AddDeviceComponent } from './add-device/add-device.component';
import { RegDeviceComponent } from './reg-device/reg-device.component';
import { DeviceDetailComponent } from './device-detail/device-detail.component';
import { DeviceStatusComponent } from './device-status/device-status.component';
import { AsyncConfirmDialogComponent } from './async-confirm-dialog/async-confirm-dialog.component';
import { AuditLogComponent } from './audit-log/audit-log.component';
import { ToolTipComponent } from './tool-tip/tool-tip.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { XhrProgressLoaderComponent } from './shared/xhr-progress-loader/xhr-progress-loader.component';
import { JasperReportComponent } from './jasper-report/jasper-report.component';
import { AboutComponent } from './about/about.component';
import { WordSeparatorPipe } from './shared/pipes/word-separator.pipe';
import { ValueAlterPipe } from './shared/pipes/value-alter.pipe';
import { ViewDeviceComponent } from './view-device/view-device.component';
import { AutofocusDirective } from './shared/directives/autofocus.directive';
import { AddEditDeviceComponent } from './add-edit-device/add-edit-device.component';
import { EmptyPipe } from './shared/pipes/empty.pipe';
import { ConnectInputAutoEditComponent } from './shared/connect-input-fields/connect-input-auto-edit/connect-input-auto-edit.component';
import { CustomMessageSnackbarComponent } from './shared/custom-message-snackbar/custom-message-snackbar.component';
import { ResponseProgressComponent } from './shared/response-progress/response-progress.component';
import { IlluminaSequencerComponent } from './view-device/device-types/illumina-sequencer/illumina-sequencer.component';
import { BackupandrestoreComponent } from './backupandrestore/backupandrestore.component';
import { ProblemReportComponent } from './problem-report/problem-report.component';
import { GlobalSearchModule } from './global-search/global-search.module';
import { NotificationtoasterService } from './services/notificationtoaster.service';
import { AppNotificationService } from './services/appnotification.service';
import { NotificationtoasterComponent } from './shared/notificationtoaster/notificationtoaster.component';
import { LabSettingsComponent } from './lab-settings/lab-settings.component';
import { ReverseShowMoreComponent } from './shared/reverse-show-more/reverse-show-more.component';
import { PermissionService } from './services/permission.service';
import { InfinitePageScrollModule } from './infinite-page-scroll/infinite-page-scroll.module';


export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, '/ui-conf/i18n/lang.', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    AddUserComponent,
    EditUserComponent,
    UserListComponent,
    AdminUiHeaderComponent,
    UserTableComponent,
    SettingsComponent,
    MyProfileComponent,
    ConfirmDialogComponent,
    AsyncConfirmDialogComponent,
    ResetPasswordComponent,
    ForgotPasswordComponent,
    CustomSnackBarComponent,
    MinMaxDirective,
    ShowMoreComponent,
    SelectLangComponent,
    LogoutComponent,
    TrimDirective,
    DeviceListComponent,
    DeviceComponent,
    AddDeviceComponent,
    DeviceDetailComponent,
    ViewDeviceComponent,
    DeviceStatusComponent,
    RegDeviceComponent,
    AuditLogComponent,
    ToolTipComponent,
    ChangePasswordComponent,
    XhrProgressLoaderComponent,
    JasperReportComponent,
    AboutComponent,
    WordSeparatorPipe,
    ValueAlterPipe,
    AutofocusDirective,
    AddEditDeviceComponent,
    EmptyPipe,
    ConnectInputAutoEditComponent,
    CustomMessageSnackbarComponent,
    ResponseProgressComponent,
    IlluminaSequencerComponent,
    BackupandrestoreComponent,
    ProblemReportComponent,
    NotificationtoasterComponent,
    LabSettingsComponent,
    ReverseShowMoreComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    MatButtonModule,
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
    MatSnackBarModule,
    MatDialogModule,
    HttpClientModule,
    MdePopoverModule,
    ReactiveFormsModule,
    MatSortModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    FormsModule,
    GlobalSearchModule,
    InfinitePageScrollModule,
    TranslateModule.forRoot(
      {
        loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient]
        }
      }
    ),
    AppRoutingModule
  ],
  entryComponents: [
    ConfirmDialogComponent,
    CustomSnackBarComponent,
    AddDeviceComponent,
    AsyncConfirmDialogComponent,
    XhrProgressLoaderComponent,
    CustomMessageSnackbarComponent,
    NotificationtoasterComponent
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    {
      provide: HTTP_INTERCEPTORS,
      useClass: XhrInterceptorService,
      multi: true
    },
    AppPropService,
    IdealService,
    AuthGuardService,
    SharedAuthService,
    NotificationtoasterService,
    AppNotificationService,
    PermissionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
