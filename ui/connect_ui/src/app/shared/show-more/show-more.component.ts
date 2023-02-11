import { Component, OnInit, Input, ChangeDetectorRef, AfterViewChecked, HostListener } from '@angular/core';

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
  @Input() isNotMaterialTooltip = true;
  public position = {
    'placement': 'bottom'
  };

  @HostListener('mousemove', ['$event']) onMousemove(event: MouseEvent) {
    console.log(event);
    if (event.clientY > 600) {
      this.position.placement = 'top';
    } else {
      this.position.placement = 'bottom';
    }
   }


  constructor(private _cdr: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngAfterViewChecked() {
    // console.log(typeof(this.data),"data")
    if (this.data && this.data.length > this.dataLength) {
      this.data = this.data.substr(0, this.dataLength).concat('...');
      this._cdr.detectChanges();
    } else if ((!this.data && this.data === '') || !this.data && this.nullCheck) {
      this.data = '-';
      this._cdr.detectChanges();
    }
  }
}
