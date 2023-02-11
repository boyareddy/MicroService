import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-fileupload-errors',
  templateUrl: './fileupload-errors.component.html',
  styleUrls: ['./fileupload-errors.component.scss']
})

@Component({
  selector: 'app-fileupload-errors',
  templateUrl: './fileupload-errors.component.html',
  styleUrls: ['./fileupload-errors.component.scss']
})
export class FileuploadErrorsComponent implements OnInit {

  @Input() errors: any;
  // tslint:disable-next-line:no-output-on-prefix
  @Output() onReTrying: EventEmitter<boolean> = new EventEmitter();

  errorRowIds: any = [];

  constructor() { }

  ngOnInit() {
  }

  onClickingReTry() {
    this.onReTrying.emit(true);
  }
}
