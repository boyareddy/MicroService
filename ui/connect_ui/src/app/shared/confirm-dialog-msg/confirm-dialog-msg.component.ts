import { Component, OnInit, EventEmitter, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-confirm-dialog-msg',
  templateUrl: './confirm-dialog-msg.component.html',
  styleUrls: ['./confirm-dialog-msg.component.scss']
})
export class ConfirmDialogMsgComponent implements OnInit {

  confirm: EventEmitter<any> = new EventEmitter();
  constructor(public dialogRef: MatDialogRef<ConfirmDialogMsgComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private translate: TranslateService) { }

  ngOnInit() {
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }


  public onConfirm(): void {
    this.confirm.emit(this.data);
  }
}
