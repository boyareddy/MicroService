import { Component, OnInit } from '@angular/core';
import { SimulatorService } from '../simulator.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {

  public devicesList: any = [];
  public showHideAddDevice = false;
  public deviceData = this._fb.group({
    id: null,
    deviceName : ['', Validators.required],
    processStepName: ['', Validators.required],
    imagePath: ['', Validators.required]
  });

  constructor(private _service: SimulatorService,
              private _fb: FormBuilder,
              private _router: Router) { }

  ngOnInit() {
    this.getDeviceList();
  }

  getDeviceList() {
    this._service.getDeviceList().subscribe(response => {
      this.devicesList = response;
    }, error => {
      console.log(error);
    });
  }

  AddDevice(deviceInfo: FormGroup) {
    console.log(deviceInfo.value);
    this._service.addDevice(deviceInfo.value).subscribe(respose => {
      console.log(respose);
      this.getDeviceList();
      this.showHideAddDevice = false;
    }, error => {
      console.log(error);
    });
  }

  showInformation(device) {
    this._router.navigate(['deviceInfo', device.id]);
  }

}
