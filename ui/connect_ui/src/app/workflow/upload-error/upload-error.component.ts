import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-upload-error',
  templateUrl: './upload-error.component.html',
  styleUrls: ['./upload-error.component.scss']
})
export class UploadErrorComponent implements OnInit {

  @Input() errors: any;
  // tslint:disable-next-line:no-output-on-prefix
  @Output() onReTrying: EventEmitter<boolean> = new EventEmitter();

  errorRowIds: any = [];

  constructor() { }

  ngOnInit() {
    // To get the row Ids
    this.errorRowIds = Object.keys(this.errors);
    // console.log(JSON.parse(this.errors));
  }

  onClickingReTry() {
    this.onReTrying.emit(true);
  }
}
