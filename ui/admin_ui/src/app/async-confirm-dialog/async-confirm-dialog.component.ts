import { Component, OnInit, Inject, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-async-confirm-dialog',
  templateUrl: './async-confirm-dialog.component.html',
  styleUrls: ['./async-confirm-dialog.component.scss']
})
export class AsyncConfirmDialogComponent implements OnInit {
  confirm: EventEmitter<any> = new EventEmitter();
  dataToPass: any;
  constructor(public dialogRef: MatDialogRef<AsyncConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

  public onNoClick(): void {
    this.dialogRef.close({action: false, parentComp: this.data.component});
  }

  public onConfirm(): void {
    this.dialogRef.close({action: true, parentComp: this.data.component});
  }
}
