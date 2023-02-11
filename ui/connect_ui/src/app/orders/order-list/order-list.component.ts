import { AfterViewChecked, Component, Input, OnChanges, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';
import { Router } from '@angular/router';
import * as moment from 'moment-timezone';

import { clearLocal, MULTI_NAV, setLocalObject } from '../../shared/utils/local-storage.util';

@Component({
    selector: 'app-order-list',
    templateUrl: './order-list.component.html',
    styleUrls: ['./order-list.component.scss']
})

export class OrderListComponent implements OnChanges, OnInit, OnDestroy, AfterViewChecked {
    @ViewChild(MatSort) sort: MatSort;
    @Input('unassignedOrders') unassignedOrders;
    dataSource: MatTableDataSource<any>;
    // unassignedOrders;
    displayedColumns: string[] = ['accessioningId', 'assayType', 'sampleType', 'priority','updatedDateTime', 'Symbol'];
    selectedId = '';
    subcribleUnassignedOrders;

    constructor(private _router: Router) {}

    ngOnChanges() {
        this.dataSource = new MatTableDataSource(this.unassignedOrders);
        this.dataSource.sort = this.sort;
    }

    ngOnInit() {
        clearLocal(MULTI_NAV.ORDER_DETAIL_PREV);
    }


    ngAfterViewChecked() {
        if (this.dataSource) {
          this.dataSource.sort = this.sort;
          this.dataSource.sortingDataAccessor = (data, header) => {
            switch (header) {
                case 'modifiedDateTime': return new Date(data.updatedDateTime);
                default: return data[header];
            }
          };
        }
      }

    ngOnDestroy() {
        if (this.subcribleUnassignedOrders !== undefined) {
          this.subcribleUnassignedOrders.unsubscribe();
        }
    }

    /**
     * Setting the selected item information in sessionStorage.
     * navigating to order details page.
     */
    showSelectedItem(element) {
        this.selectedId = element.accessioningId;
        sessionStorage.setItem('selectedItem', element.orderId);
        setLocalObject(MULTI_NAV.ORDER_DETAIL_PREV, this._router.url)
        this._router.navigate(['orders', 'order-details', element.orderId]);
    }

    /**
     * Getting the time zone by providing the timezone offset value to moment.
     */
    getTimeZone(oldDate) {
        const timeZone = moment.tz.guess();
        const currentTime = new Date(oldDate);
        const timeZoneOffset = currentTime.getTimezoneOffset();
        return moment.tz.zone(timeZone).abbr(timeZoneOffset);
    }

}
