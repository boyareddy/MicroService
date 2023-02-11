import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptorService } from './shared/http-request-interceptor.service';
import { VersionComponent } from './version/version.component';
import { GlobalSearchComponent } from './global-search/components/global-search/global-search.component';


const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/login'
  },
  {
    path: 'login',
    loadChildren: './auth/auth.module#AuthModule'
  },
  {
    path: 'overview',
    loadChildren: './overview/overview.module#OverviewModule'
  },
  {
    path: 'orders',
    loadChildren: './orders/orders.module#OrdersModule'
  },
  {
    path: 'workflow',
    loadChildren: './workflow/workflow.module#WorkflowModule'
  },
  {
    path: 'notification',
    loadChildren: './notification/notification.module#NotificationModule'
  },
  {
    path: 'version',
    component: VersionComponent
  },
  {
    path: 'search',
    loadChildren: './global-search/global-search.module#GlobalSearchModule'
  },
  {
    path: 'rsr-login',
    loadChildren: './rsr-login/rsr-login.module#RsrLoginModule'
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    }

  ]
})
export class AppRoutingModule { }
