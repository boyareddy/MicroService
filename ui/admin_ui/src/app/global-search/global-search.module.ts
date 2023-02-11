import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GlobalSearchComponent } from './components/global-search/global-search.component';
import { FormsModule } from '@angular/forms';
import { MatIconModule, MatExpansionModule, MatTableModule, MatProgressSpinnerModule } from '@angular/material';
import { TranslateModule } from '@ngx-translate/core';
import { OrderTableComponent } from './components/order-table/order-table.component';
import { RunTableComponent } from './components/run-table/run-table.component';
import { StatusIconComponent } from './components/status-icon/status-icon.component';
import { FlagPopupComponent } from './components/flag-popup/flag-popup.component';
import { MdePopoverModule } from '@material-extended/mde';
import { ResponseProgressComponent } from './components/response-progress/response-progress.component';
import { HighlightDirective } from './directives/highlight.directive';
import { PluralCheckPipe } from './directives/plural-check.pipe';
import { DateTimeZoneDirective } from '../directives/date-time-zone.directive';
import { InfinitePageScrollModule } from '../infinite-page-scroll/infinite-page-scroll.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    MatExpansionModule,
    MatTableModule,
    TranslateModule,
    MdePopoverModule,
    MatProgressSpinnerModule,
    InfinitePageScrollModule
  ],
  declarations: [GlobalSearchComponent, OrderTableComponent, RunTableComponent, StatusIconComponent, FlagPopupComponent, ResponseProgressComponent, HighlightDirective, PluralCheckPipe, DateTimeZoneDirective],
  exports: [GlobalSearchComponent]
})
export class GlobalSearchModule { }