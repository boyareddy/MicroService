import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { HeaderMenuComponent } from './header-menu/header-menu.component';
import { SharedService } from './shared.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


/**
 * Angular Material Modules
*/
 import { MaterialModule } from './material.module';
import { HttpClientModule } from '@angular/common/http';
import { AuthGuardService } from '../auth/AuthService/auth-guard.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptorService } from './http-request-interceptor.service';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { TranslateModule } from '@ngx-translate/core';
import { MinMaxDirective } from './directives/min-max.directive';
import { ShowMoreDirective } from './directives/show-more.directive';
import { ShowMoreComponent } from './show-more/show-more.component';
import { SelectLangComponent } from './select-lang/select-lang.component';
import { ApiUrlService } from './service-urls/apiurl.service';
import { LazyLoadingComponent } from './lazy-loading/lazy-loading.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { LogoutComponent } from './logout/logout.component';
import { TrimDirective } from './directives/trim-directive';
import { XhrProgressLoaderComponent } from './xhr-progress-loader/xhr-progress-loader.component';
import { DateTimeZoneDirective } from './directives/date-time-zone.directive';
import { EmptyDataCheck } from './pipes/emptyDataCheck.pipe';
import { FlagPopupComponent } from './flag-popup/flag-popup.component';
import { FileuploadErrorsComponent } from './fileupload-errors/fileupload-errors.component';
import { ConfirmDialogMsgComponent } from './confirm-dialog-msg/confirm-dialog-msg.component';
import { PluralCheckPipe } from './pipes/plural-check.pipe';
import { ShowCenterEllipsesComponent } from './show-center-ellipses/show-center-ellipses.component';
import { CustomSnackbarComponent } from './custom-snackbar/custom-snackbar.component';
import { CommonApiService } from './common-api.service';
import { NotificationService } from '../notification/notification.service';
import { ResponseProgressComponent } from './response-progress/response-progress.component';
import { InlineEditComponent } from './inline-edit/inline-edit.component';
import { SplitPipe } from './pipes/split.pipe';
import { AppNotificationService } from './appnotification.service';
import { NotificationtoasterComponent } from './notificationtoaster/notificationtoaster.component';
import { ReverseShowMoreComponent } from './reverse-show-more/reverse-show-more.component';
import { FilterComponent } from './filter/filter.component';
import { FilterPipe } from './filter/filter.pipe';
import { FilterIconComponent } from './filter/filter-icon/filter-icon.component';
import { AuthService } from '../auth/AuthService/auth.service';
import { MatIconInfoService } from './mat-icons/mat-icon-info.service';
import { AppWarningRequiredComponent } from './inline-htmls/warning-required.component';
import { InfinitePageScrollModule } from './confirm-dialog/infinite-page-scroll/infinite-page-scroll.module';
import { ShowMoreMultilineComponent } from './show-more-multiline/show-more-multiline.component';

@NgModule({
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
    TranslateModule,
    ReactiveFormsModule,
    InfinitePageScrollModule
  ],
  declarations: [
    HeaderComponent,
    HeaderMenuComponent,
    ConfirmDialogComponent,
    ConfirmDialogMsgComponent,
    MinMaxDirective,
    ShowMoreDirective,
    ShowMoreComponent,
    SelectLangComponent,
    LazyLoadingComponent,
    LogoutComponent,
    TrimDirective,
    XhrProgressLoaderComponent,
    DateTimeZoneDirective,
    EmptyDataCheck,
    FlagPopupComponent,
    FileuploadErrorsComponent,
    ShowCenterEllipsesComponent,
    PluralCheckPipe,
    CustomSnackbarComponent,
    ResponseProgressComponent,
    InlineEditComponent,
    SplitPipe,
    NotificationtoasterComponent,
    ReverseShowMoreComponent,
    FilterComponent,
    FilterPipe,
    FilterIconComponent,
    AppWarningRequiredComponent,
    ShowMoreMultilineComponent
  ],
  exports: [
    HeaderComponent,
    HeaderMenuComponent,
    MaterialModule,
    FormsModule,
    ConfirmDialogComponent,
    ConfirmDialogMsgComponent,
    TranslateModule,
    MinMaxDirective,
    ShowMoreDirective,
    ShowMoreComponent,
    SelectLangComponent,
    LazyLoadingComponent,
    LogoutComponent,
    TrimDirective,
    XhrProgressLoaderComponent,
    DateTimeZoneDirective,
    EmptyDataCheck,
    FlagPopupComponent,
    FileuploadErrorsComponent,
    ShowCenterEllipsesComponent,
    PluralCheckPipe,
    CustomSnackbarComponent,
    ResponseProgressComponent,
    InlineEditComponent,
    SplitPipe,
    NotificationtoasterComponent,
    ReverseShowMoreComponent,
    FilterComponent,
    ShowMoreMultilineComponent,
    ReactiveFormsModule,
    FilterPipe,
    FilterIconComponent,
    AppWarningRequiredComponent,
    InfinitePageScrollModule
  ],
  providers: [
    AuthGuardService,
    AuthService,
    NotificationService,
    CommonApiService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    },
    AppNotificationService,
    MatIconInfoService
  ],
  entryComponents: [ ConfirmDialogComponent,
    XhrProgressLoaderComponent,
    FlagPopupComponent,
    ConfirmDialogMsgComponent,
    CustomSnackbarComponent,
    NotificationtoasterComponent ]
})
export class SharedModule { }
