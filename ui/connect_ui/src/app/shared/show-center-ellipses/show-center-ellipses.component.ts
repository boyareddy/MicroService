import { Component, OnInit, Input, AfterViewChecked, ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-show-center-ellipses',
  templateUrl: './show-center-ellipses.component.html',
  styleUrls: ['./show-center-ellipses.component.scss']
})
export class ShowCenterEllipsesComponent implements OnInit, AfterViewChecked {

  @Input() dataLength = 27;
  @Input() tooltipValue: string;
  @Input() data: any;
  @Input() displayFull = false;
  @Input() nullCheck = false;

  constructor(private _cdr: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngAfterViewChecked() {
    if (this.data && this.data.length > this.dataLength) {
      const substr1 = this.data.substr(0, 13).concat('...');
      const substr2 = this.data.substr(this.data.length - 13 , this.data.length);
      this.data = substr1 + substr2;
      this._cdr.detectChanges();
    } else if (((this.data && this.data.trim() === '') || !this.data) && this.nullCheck) {
      this.data = '--';
      this._cdr.detectChanges();
    }
  }

}
