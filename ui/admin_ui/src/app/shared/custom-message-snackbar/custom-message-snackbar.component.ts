import { Component, OnInit, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-custom-message-snackbar',
  templateUrl: './custom-message-snackbar.component.html',
  styleUrls: ['./custom-message-snackbar.component.scss']
})
export class CustomMessageSnackbarComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any, private _snackBar: MatSnackBar) {
    console.log(data);
   }


  ngOnInit() {
  }

  public closeSnackbar() {
    this._snackBar.dismiss();
  }
}
