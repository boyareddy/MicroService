import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-ongoing',
  templateUrl: './ongoing.component.html',
  styleUrls: ['./ongoing.component.scss']
})
export class OngoingComponent implements OnInit {

  @Input('item') item: any;
  constructor() { }

  ngOnInit() {
  }

}
