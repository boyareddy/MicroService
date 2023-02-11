import { Component, OnInit, Input, OnChanges } from '@angular/core';
import * as moment from 'moment-timezone';
import { OrderService } from '../../order.service';
import { Router } from '@angular/router';
import { FlagService } from '../../../shared/flag-popup/flag.service';
import { StatusIcons } from '../../util/Constants';
import { SharedService } from 'src/app/shared/shared.service';

@Component({
  selector: 'app-work-flow',
  templateUrl: './work-flow.component.html',
  styleUrls: ['./work-flow.component.scss']
})
export class WorkFlowComponent implements OnInit, OnChanges {

  @Input()
  workFlowDetails;
  molicularID;
  public pendingImage = StatusIcons.RUN_PENDING;

  flagIconList = [];

  constructor(private _sharedService: SharedService,
    private router: Router,
    private _flagSvc: FlagService) { }

  ngOnInit() {
  }

  ngOnChanges() {
    console.log('workFlowDetails', this.workFlowDetails);
    if (this.workFlowDetails) {
      this.workFlowDetails.forEach(workFlow => {
        this.getFlagIcons(workFlow);
      });
    }
    console.log('flagIconList', this.flagIconList);
  }

  getFlagIcons(sample) {
    const flagIcon: string = this._flagSvc.getSeverity(sample);
    this.flagIconList[sample.processStepName] = flagIcon;
  }

  /**
   * Getting the time from server. Converting into date and time.
   * Finding the zone by momment-timezone.
   * Sedding back to view.
   */
  getTimeZone(oldDate) {
    if (oldDate != null && oldDate !== undefined) {
      const timeZone = moment.tz.guess();
      const currentTime = new Date(oldDate);
      const timeZoneOffset = currentTime.getTimezoneOffset();
      return moment.tz.zone(timeZone).abbr(timeZoneOffset);
    }
  }

  navigate(queryArray) {
    this._sharedService.setData('isWorkflowData', true);
    this.router.navigate(queryArray);
  }



}
