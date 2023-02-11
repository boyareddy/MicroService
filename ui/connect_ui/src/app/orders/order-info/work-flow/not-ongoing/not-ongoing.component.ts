import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-not-ongoing',
  templateUrl: './not-ongoing.component.html',
  styleUrls: ['./not-ongoing.component.scss']
})
export class NotOngoingComponent implements OnInit {

  @Input('item') item: any;
  constructor() { }

  ngOnInit() {
  }

}
