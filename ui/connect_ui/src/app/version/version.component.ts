import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment.prod';

@Component({
  selector: 'app-version',
  templateUrl: './version.component.html',
  styleUrls: ['./version.component.scss']
})
export class VersionComponent implements OnInit {

  version: string;
  constructor() { }

  ngOnInit() {
    console.log('Connect UI Version : ', environment.version);
    this.version = environment.version;
  }

}
