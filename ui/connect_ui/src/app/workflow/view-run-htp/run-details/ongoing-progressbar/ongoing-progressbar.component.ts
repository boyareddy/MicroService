import { Component, OnInit, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'app-ongoing-progressbar',
  templateUrl: './ongoing-progressbar.component.html',
  styleUrls: ['./ongoing-progressbar.component.scss']
})
export class OngoingProgressbarComponent implements OnChanges {

  @Input() progressbar;

  constructor() { }

  ngOnChanges() {
    // tslint:disable-next-line:no-unused-expression
    console.log(this.progressbar, 'progressbar');
  }

}
