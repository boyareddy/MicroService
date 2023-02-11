import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment-timezone';
import { PermissionService } from 'src/app/shared/permission.service';
import { RequiredFieldValidationsForOrder } from 'src/app/standard-names/constants';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit {

  @Input() orderDetails;
  @Input() processStepNameInfo: string;
  @Input() patientConfigFile: any;
  @Input() assayType: string;

  havingAccess = false;

  public comments = RequiredFieldValidationsForOrder.COMMENTS;

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
    const orderStep = 0;
    this._route.navigate(['orders', 'edit-order', orderIdLocalStorage, orderStep]);
  }

  /**
   * Getting the time from server. Converting into date and time.
   * Finding the zone by momment-timezone.
   * Sedding back to view.
   */
  getTimeZone(oldDate) {
    const timeZone = moment.tz.guess();
    const currentTime = new Date(oldDate);
    const timeZoneOffset = currentTime.getTimezoneOffset();
    return moment.tz.zone(timeZone).abbr(timeZoneOffset);
  }

}
