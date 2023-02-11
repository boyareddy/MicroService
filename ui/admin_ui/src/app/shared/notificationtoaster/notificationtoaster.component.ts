import { Component, OnInit, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-notificationtoaster',
  templateUrl: './notificationtoaster.component.html',
  styleUrls: ['./notificationtoaster.component.scss']
})
export class NotificationtoasterComponent implements OnInit {

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: any,
    private _snackBar: MatSnackBar,
    private _router: Router,
    private _SS: SharedService
  ) { }

  ngOnInit() {
  }

  closeSnackbar(event: Event) {
    event.stopPropagation();
    this._snackBar.dismiss();
  }

  gotoNotification(val) {
    if (val) {
      this._SS.setSharedData('isToasterClosed', true);
      if (val.multmsg) {
        this._snackBar.dismiss();
        window.location.href = '/connect_ui/#/notification';
      } else {
        sessionStorage.setItem('selectedNotf', JSON.stringify(val));
        this._snackBar.dismiss();
        window.location.href = '/connect_ui/#/notification';
      }
    }
  }

  isMultiple(val) {
    if (val.multmsg) {
      return true;
    } else {
      return false;
    }
  }

}
