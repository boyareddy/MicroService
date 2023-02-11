import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'xhr-progress-loader',
  templateUrl: './xhr-progress-loader.component.html',
  styleUrls: ['./xhr-progress-loader.component.scss']
})
export class XhrProgressLoaderComponent implements OnInit {

  afterXSec: boolean = false;

  constructor() { }

  ngOnInit() {
    setTimeout(timeOut => {
      this.afterXSec = true;
    }, 1000);
  }

}
