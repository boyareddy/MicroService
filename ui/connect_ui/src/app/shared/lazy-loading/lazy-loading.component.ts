import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-lazy-loading',
  templateUrl: './lazy-loading.component.html',
  styleUrls: ['./lazy-loading.component.scss']
})
export class LazyLoadingComponent implements OnInit {

  @Input() scrollDistance: number = 2;
  @Input() scrollThrottle: number = 50;

  @Output() onScroll: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  /**
   * onScroll calls when user scrolls the scrol.
   */
  public onPageScroll(){
    //console.log('Onscroll');
    this.onScroll.emit();
  }
}
