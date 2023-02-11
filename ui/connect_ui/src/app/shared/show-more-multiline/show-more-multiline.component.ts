import { Component, OnInit, Input } from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'show-more-multiline',
  templateUrl: './show-more-multiline.component.html',
  styleUrls: ['./show-more-multiline.component.scss']
})
export class ShowMoreMultilineComponent implements OnInit {

  // tslint:disable-next-line:no-input-rename
  @Input('show-lines') showLines: number;
  @Input('data') data: string;

  constructor() { }

  ngOnInit() {
  }

}
