import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { Router } from '@angular/router';
import { PermissionService } from 'src/app/shared/permission.service';
import { RequiredFieldValidationsForOrder } from 'src/app/standard-names/constants';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-assay-details',
  templateUrl: './assay-details.component.html',
  styleUrls: ['./assay-details.component.scss']
})
export class AssayDetailsComponent implements OnChanges, OnInit {

  @Input() assayDetails;
  @Input() processStepNameInfo;
  @Input() patientConfigFile;
  @Input() assayType: string;

  objectKeys = Object.keys;
  havingAccess = false;
  public is_gestational_ageFieldValid = false;
  public is_maternalAgeFieldValid = false;

  public maternalAge = RequiredFieldValidationsForOrder.MATERNAL_AGE;
  public ivf_status = RequiredFieldValidationsForOrder.IVF_STATUS;
  public gestational_age = RequiredFieldValidationsForOrder.GESTATIONAL_AGE;
  public egg_donor = RequiredFieldValidationsForOrder.EGG_DONOR;
  public egg_donor_age = RequiredFieldValidationsForOrder.EGG_DONOR_AGE;
  public number_of_fetus = RequiredFieldValidationsForOrder.NUMBER_OF_FETUS;
  public missingText;

  constructor(
    private _route: Router,
    private _permission: PermissionService,
    private _translate: TranslateService
  ) { }

  ngOnChanges() {
    if (this.patientConfigFile && this.patientConfigFile.length > 0) {
      this.patientConfigFile.find(element => {
        if (element.fieldName === this.maternalAge) {
          this.is_maternalAgeFieldValid = true;
        } else if (element.fieldName === this.gestational_age) {
          this.is_gestational_ageFieldValid = true;
        }
      });
  } else {
    this.is_maternalAgeFieldValid = false;
    this.is_gestational_ageFieldValid = false;
  }
}

  ngOnInit() {
    this._permission.checkPermissionObs('Update_Order').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    const translationInfo = this._translate.translations[this._translate.currentLang];
    this.missingText = translationInfo.orders.missing;
  }

  /**
   * Called when edit link clicked.
   * Setting the order step value in service.
   * Navigating to order edit page.
  */
  editDetails() {
     // tslint:disable-next-line:radix
    const orderIdLocalStorage: number = parseInt(sessionStorage.getItem('selectedItem'));
    const orderStep = 1;
    this._route.navigate(['orders', 'edit-order', orderIdLocalStorage, orderStep]);
  }

}
