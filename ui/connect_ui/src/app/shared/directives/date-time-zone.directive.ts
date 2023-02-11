import { Directive, ElementRef, Input, OnChanges } from '@angular/core';
import * as moment from 'moment-timezone';

@Directive({
  selector: '[appDateTimeZone]'
})
export class DateTimeZoneDirective implements OnChanges {
  @Input('appDateTimeZone') appDateTimeZone: any;
  constructor(private _el: ElementRef) {
  }

  ngOnChanges() {
    const timeZone = moment.tz.guess();
    if (this.appDateTimeZone) {
      if (typeof this.appDateTimeZone === 'string') {
        this.appDateTimeZone = this.appDateTimeZone.trim();
      }
      let currentTime;
      if (!(isNaN(Date.parse(this.appDateTimeZone)))) {
        currentTime = new Date(this.appDateTimeZone);
      } else {
        currentTime = new Date();
      }
      const timeZoneOffset = currentTime.getTimezoneOffset();
      this._el.nativeElement.innerHTML = moment.tz.zone(timeZone).abbr(timeZoneOffset);
    } else {
      this._el.nativeElement.innerHTML = '';
    }
  }

}
