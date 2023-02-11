import { Component, OnInit, Input } from '@angular/core';
import { setCss } from '../utils/device-icon.util';

@Component({
  selector: 'app-device-status',
  template: `
    <div [ngClass]="statusCss + ' status-circle'"></div>
  `,
  styles: [`
    .status-circle{
      height: 10px; width: 10px; border-radius: 50%;
    }
  `]
})
export class DeviceStatusComponent implements OnInit {

  @Input() status: any;

  statusCss: string;

  constructor() { }

  ngOnInit() {
    console.log(this.status);
    // Set the css based on status.
    this.statusCss = setCss(this.status).concat('_circle');
  }

}
