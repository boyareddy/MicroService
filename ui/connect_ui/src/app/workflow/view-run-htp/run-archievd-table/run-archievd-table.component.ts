import { Component, OnInit, OnChanges, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort } from '@angular/material';
import * as moment from 'moment-timezone';
import { setLocal, MULTI_NAV, setLocalObject } from '../../../shared/utils/local-storage.util';
import { Router } from '@angular/router';
import { SharedService } from '../../../shared/shared.service';

@Component({
  selector: 'app-run-archievd-table',
  templateUrl: './run-archievd-table.component.html',
  styleUrls: ['./run-archievd-table.component.scss']
})
export class RunArchievdTableComponent implements OnChanges, OnInit {

  // tslint:disable-next-line:no-input-rename
  @Input('archiveTblInfo') archiveTblInfo;
  @ViewChild(MatSort) sort: MatSort;
  public dataSource: MatTableDataSource<any>;

  selectedId = '';
  displayedColumns: string[] = ['deviceRunId', 'processStepName', 'totalSamplecount', 'operatorName', 'runCompletionTime', 'symbol' ];
  constructor(
    private _router: Router,
    private _sharedService: SharedService
  ) {
  }

  ngOnChanges() {
   // console.log(this.archiveTblInfo, 'tbl Info');
   this.dataSource = new MatTableDataSource(this.archiveTblInfo);
    this.dataSource.sort = this.sort;
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, header) => {
      if (header === 'runCompletionTime') {
        return new Date(data.runCompletionTime);
      } else {
        return data[header];
      }
      // switch (header) {
      //     case 'runCompletionTime': return new Date(data.runCompletionTime);
      //     default: return data[header];
      // }
    };
  }

  ngOnInit() {
    // alert('hi');
   // clearLocal(MULTI_TAB_NAV.COMPLETED_ORDER_PREV);
  }

   /**
     * Getting the time zone by providing the timezone offset value to moment.
     */
    getTimeZone(oldDate) {
      console.log(oldDate, 'oldDate ====');
      const timeZone = moment.tz.guess();
      const currentTime = new Date(oldDate);
      const timeZoneOffset = currentTime.getTimezoneOffset();
      console.log(moment.tz.zone(timeZone).abbr(timeZoneOffset));
      return moment.tz.zone(timeZone).abbr(timeZoneOffset);
  }

   /**
     * Setting the selected item information in sessionStorage.
     * navigating to order details page.
     */
    showSelectedItem(element) {
     sessionStorage.setItem('selectedRow', 'Completed tab');
     this._router.navigate(['workflow', 'rundetails', element.runResultId]);
  }

}
