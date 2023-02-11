import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GlobalSearchComponent } from './components/global-search/global-search.component';
import { FormsModule } from '@angular/forms';
import { MatIconModule, MatExpansionModule, MatTableModule } from '@angular/material';
import { TranslateModule } from '@ngx-translate/core';
import { OrderTableComponent } from './components/order-table/order-table.component';
import { RunTableComponent } from './components/run-table/run-table.component';
import { StatusIconComponent } from './components/status-icon/status-icon.component';
import { MdePopoverModule } from '@material-extended/mde';
import { SearchService } from './services/search.service';
import { SharedModule } from '../shared/shared.module';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuardService } from '../auth/AuthService/auth-guard.service';
import { HighlightDirective } from './directive/highlight.directive';

const globalSearchRoutes: Routes = [
  {
      path: '',
      component: GlobalSearchComponent,
      canActivate: [AuthGuardService]
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    MatExpansionModule,
    MatTableModule,
    TranslateModule,
    MdePopoverModule,
    SharedModule,
    RouterModule.forChild(globalSearchRoutes)
  ],
  declarations: [GlobalSearchComponent, OrderTableComponent, RunTableComponent, StatusIconComponent, HighlightDirective],
  providers: [SearchService],
  exports: [GlobalSearchComponent]
})
export class GlobalSearchModule { }