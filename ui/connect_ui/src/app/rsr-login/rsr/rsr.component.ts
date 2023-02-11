import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RsrService } from '../rsr.service';
import { Constant_RSR_Names } from '../rsr-util';
import { HttpErrorResponse } from '@angular/common/http';
import { HeaderInfo } from 'src/app/shared/header.model';
import { regx } from '../../shared/utils/regx.const';
import { vldSpecialCar, vldRequired, vldSpaceCar, vldAlphaNum, vldRequiredSelect } from '../../shared/utils/validation.util';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rsr',
  templateUrl: './rsr.component.html',
  styleUrls: ['./rsr.component.scss']
})
export class RsrComponent implements OnInit {

  public rsrLogin: FormGroup;
  public successStatus: string;
  public failedStatus: string;
  headerInfo: HeaderInfo = {
    headerName: 'Connect workflow manager',
    isLoginPage: true
  };

  constructor(
    private _formBuilder: FormBuilder,
    private _rsrService: RsrService,
    private _router: Router
  ) { }

  ngOnInit() {
    this.rsrLogin = this._formBuilder.group({
      // tslint:disable-next-line:max-line-length
      'rocheId': ['',
        Validators.compose([vldRequired('RSR_LOGIN.VALIDATION_ERROR.ROCHE_ID_REQUIRED')])
      ],
      'token': ['',
      Validators.compose([vldRequired('RSR_LOGIN.VALIDATION_ERROR.ROCHE_ID_TOKEN')])
    ]
    });
  }

  onBack() {
this._router.navigate(['login']);
  }

  onLogin() {
    this.successStatus = null;
    this.failedStatus = null;
    const info = this.rsrLogin.value;
    this._rsrService.rsrLogin(info).subscribe(response => {
      console.log(response);
      this.successStatus = Constant_RSR_Names.SUCCESS;
    }, (error: HttpErrorResponse) => {
     if (error.status === 403) {
        this.failedStatus = Constant_RSR_Names.FORBIDDEN;
        alert(this.failedStatus);
      } else {
        this.failedStatus = Constant_RSR_Names.FAILED;
      }
      console.log(error);
    });
    console.log(this.successStatus, this.failedStatus);
  }

}
