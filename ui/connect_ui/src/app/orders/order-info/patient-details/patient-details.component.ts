import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { PermissionService } from 'src/app/shared/permission.service';
import { RequiredFieldValidationsForOrder } from 'src/app/standard-names/constants';

@Component({
  selector: 'app-patient-details',
  templateUrl: './patient-details.component.html',
  styleUrls: ['./patient-details.component.scss']
})
export class PatientDetailsComponent implements OnInit {

  @Input() patientDetails;
  @Input() processStepNameInfo: string;
  @Input() patientConfigFile: any;
  @Input() assayType: string;

  havingAccess = false;

  public first_name = RequiredFieldValidationsForOrder.FIRST_NAME;
  public last_name = RequiredFieldValidationsForOrder.LAST_NAME;
  public date_of_birth = RequiredFieldValidationsForOrder.DOB;
  public medical_rec_number = RequiredFieldValidationsForOrder.MEDICAL_REC_NUMBER;
  public lab_id = RequiredFieldValidationsForOrder.LABORATORY_ID;
  public acc_number = RequiredFieldValidationsForOrder.ACCOUNT_NUMBER;
  public other_clinician = RequiredFieldValidationsForOrder.OTHER_CLINICIAN;
  public clinic_name = RequiredFieldValidationsForOrder.CLINIC_NAME;
  public referring_clinician = RequiredFieldValidationsForOrder.REFERRING_CLINICIAN;
  public reason_for_referral = RequiredFieldValidationsForOrder.REASON_FOR_REFERRAL;

  constructor(
    private _route: Router,
    private _permission: PermissionService
  ) { }

  ngOnInit() {
    this._permission.checkPermissionObs('Update_Order').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
  }

  /**
   * Called when edit link clicked.
   * Setting the order step value in service.
   * Navigating to order edit page.
   */
  editDetails() {
    // tslint:disable-next-line:radix
    const orderIdLocalStorage: number = parseInt(sessionStorage.getItem('selectedItem'));
    const orderStep = 2;
    this._route.navigate(['orders', 'edit-order', orderIdLocalStorage, orderStep]);
  }

}
