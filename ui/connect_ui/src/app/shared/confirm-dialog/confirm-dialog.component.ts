import { Component, OnInit, Inject, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Location } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {
  confirm: EventEmitter<any> = new EventEmitter();

  constructor(public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    private _location: Location,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private translate: TranslateService) { }

  ngOnInit() {
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public goBack(): void {
    this._location.back();
  }

  public onConfirm(): void {
    this.confirm.emit(this.data);
  }

}
