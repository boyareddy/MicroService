import { Component, OnInit, Input, ChangeDetectorRef, AfterViewChecked } from '@angular/core';

@Component({
  selector: 'app-show-more',
  templateUrl: './show-more.component.html',
  styleUrls: ['./show-more.component.scss']
})
export class ShowMoreComponent implements OnInit, AfterViewChecked {

  @Input() dataLength = 27;
  @Input() tooltipValue: string;
  @Input() data: string;
  @Input() displayFull = false;
  @Input() nullCheck = false;
  @Input() preCheck = false;

  constructor(private _cdr: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngAfterViewChecked() {
    if (this.data && this.data.length > this.dataLength) {
      if (this.preCheck) {
        this.data = this.data.substr(this.data.length - 27, this.data.length);
        this.data = '...'.concat(this.data);
      } else {
        this.data = this.data.substr(0, this.dataLength).concat('...');
      }
      this._cdr.detectChanges();
    } else if (((this.data && this.data.trim() === '') || !this.data) && this.nullCheck) {
      this.data = '-';
      this._cdr.detectChanges();
    }
  }
}
