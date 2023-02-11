import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SimulatorService } from '../simulator.service';

@Component({
  selector: 'app-device-info',
  templateUrl: './device-info.component.html',
  styleUrls: ['./device-info.component.css']
})
export class DeviceInfoComponent implements OnInit {

  public deviceInfo;
  constructor(private _acRoute: ActivatedRoute,
              private _service: SimulatorService
    ) { }

  ngOnInit() {
    this._acRoute.params.subscribe(params => {
      console.log(params.id);
      this.getDeviceInfo(params.id);
    });
  }

  getDeviceInfo(id) {
    this._service.getSingleDevice(id).subscribe(response => {
      this.deviceInfo = response;
    }, error => {
      console.log(error);
    });
  }

}
