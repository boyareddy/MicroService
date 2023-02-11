import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { InfinitePageScrollComponent } from './infinite-page-scroll.component';
import { MatProgressSpinnerModule } from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    InfiniteScrollModule,
    MatProgressSpinnerModule
  ],
  declarations: [
    InfinitePageScrollComponent
  ],
  exports: [
    InfinitePageScrollComponent,
    InfiniteScrollModule
  ]
})
export class InfinitePageScrollModule { }
