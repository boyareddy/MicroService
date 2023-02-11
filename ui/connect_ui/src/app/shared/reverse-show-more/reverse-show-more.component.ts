import { Component, OnInit, Input, HostListener } from '@angular/core';

@Component({
  selector: 'app-reverse-show-more',
  templateUrl: './reverse-show-more.component.html',
  styleUrls: ['./reverse-show-more.component.scss']
})
export class ReverseShowMoreComponent implements OnInit {

  @Input() tooltipValue: string;
  @Input() data: string;
  constructor() { }

  public position = {
    'placement': 'bottom'
  };

  @HostListener('mousemove', ['$event']) onMousemove(event: MouseEvent) {
    console.log(event);
    if (event.clientY > 600) {
      this.position.placement = 'top';
    } else {
      this.position.placement = 'bottom';
    }
   }
  ngOnInit() {
  }

}
