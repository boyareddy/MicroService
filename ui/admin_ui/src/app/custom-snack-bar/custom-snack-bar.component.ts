import { Component, OnInit } from '@angular/core';
import { SharedService } from './../services/shared.service';

@Component({
  selector: 'app-custom-snack-bar',
  templateUrl: './custom-snack-bar.component.html',
  styleUrls: ['./custom-snack-bar.component.scss']
})
export class CustomSnackBarComponent implements OnInit {
  messages: any;
  constructor(private _sharedSvc: SharedService) { }

  ngOnInit() {
    this.messages = this._sharedSvc.getSharedData('multiSnackBarMsgs');
  }

}
