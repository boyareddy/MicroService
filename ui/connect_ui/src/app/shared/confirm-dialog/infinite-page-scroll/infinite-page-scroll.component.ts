import { Component, Output, EventEmitter, Input } from '@angular/core';

// Models

@Component({
  selector: 'app-infinite-page-scroll',
  template: `
    <div
      class="search-results"
      infiniteScroll
      [infiniteScrollDistance]="2"
      (scrolled)="onPageScroll()"
    ></div>
    
    <div class="spin-center">
      <mat-spinner *ngIf="isXhrOnprogress"
        [value]="55"
        [diameter]= "55"
        [strokeWidth] = "5"
        >
      </mat-spinner>
    </div>
  `,
  styles: [
    '.spin-center { display: flex;justify-content: center;margin-bottom: 20px; }'
  ]
})
export class InfinitePageScrollComponent {

  scrolledPage: number = 1;

  @Input() isXhrOnprogress: boolean;

  @Output() pageScroll: EventEmitter<any> = new EventEmitter();

  constructor() { }

  onPageScroll(){
    this.scrolledPage++;
    this.pageScroll.emit(this.scrolledPage);
  }

}
