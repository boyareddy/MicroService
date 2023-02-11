import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { FlagService } from '../../../shared/flag-popup/flag.service';

@Component({
  selector: 'app-new-card',
  templateUrl: './new-card.component.html',
  styleUrls: ['./new-card.component.scss']
})
export class NewCardComponent implements OnInit {
  @Input() runCardInfo: any;

  constructor(private _router: Router, private _flagSvc: FlagService) { }
  flagIcon: string;

  ngOnInit() {
    this.flagIcon = this._flagSvc.getSeverity(this.runCardInfo);
    // console.log("runCardInfo", this.runCardInfo.flag, this.flagIcon);
  }

  getRunID(runDetails: any) {
    // console.log(runDetails);
    const {runStatus, assayType, runResultId, containerId, containertype, processStepName, wfmsflag = ''} = runDetails;
    if (wfmsflag === '' && runResultId) {
      this._router.navigate(['workflow', 'rundetails', runResultId]);
    } else if (wfmsflag === 'pending') {
      this._router.navigateByUrl('/workflow/pendingruns?assayType=' + assayType + '&processstepname=' + processStepName);
    } else if (runStatus.toLowerCase() === 'pending') {
      this._router.navigate(['/workflow/mapped-samples'],
        { queryParams: { plateNo: containertype, containerId: containerId } });
    }
  }

  navigate(deviceId: any) {
    window.location.href = `/admin_ui/#/device-detail/${deviceId}`;
  }
}
