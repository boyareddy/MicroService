import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { OrderService } from '../order.service';
import { SharedService } from '../../shared/shared.service';
import { MatSnackBar } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';
import { Router } from '../../../../node_modules/@angular/router';
import { PermissionService } from 'src/app/shared/permission.service';
import { FilterComponent } from '../../shared/filter/filter.component';
import { WarningRequiredService } from 'src/app/shared/inline-htmls/warning-required.service';

@Component({
  selector: 'app-workflowmanager-orders',
  templateUrl: './workflowmanager-orders.component.html',
  styleUrls: ['./workflowmanager-orders.component.scss']
})
export class WorkflowmanagerOrdersComponent implements OnInit, OnDestroy {
  unassignedOrders: any = [];
  inworkflowOrders: any = [];
  defaultTabIndex = 0;
  subcribleInWorkflowOrders;
  subcribleUnassignedOrders;
  havingAccess = false;
  toggleUnassignedFilter: boolean = false;
  toggleInworkflowFilter: boolean = false;
  inworkflowFilterCount: boolean = false;
  unassignedFilterCount: number;

  /* Header Component Information */
  headerInfo: HeaderInfo = {
    headerName: 'Workflow manager',
    headerIcon: 'assets/Images/header/module/Orders.svg',
    curPage: 'Orders'
  };

  @ViewChild('unassignedFilter') unassignedFilter: FilterComponent;

  /* How many orders available */
  public orderCount = {
      'unassigned': null,
      'inworkflow': null,
      'pendingrelease': null,
      'completed': null
    };


  constructor(private _service: OrderService,
    private _sharedService: SharedService,
    private _snackBar: MatSnackBar,
    private translate: TranslateService,
    private router: Router,
    private _snackBarSvc: SnackBarService,
    private _permission: PermissionService,
    private _warningService: WarningRequiredService
  ) {
    this.getUnassignedOrdersCount();
    this.getInworkflowOrdersCount();
    this.enableSnackBar();
  }

  ngOnInit() {
    if (this.unassignedFilter) {
      this.unassignedFilter.toggleComponent = true;
    }
   const prevPage = this._sharedService.getData('navCurPage');
   this._warningService.getLastProcessStepName();
    if (prevPage && prevPage === 'In Workflow') {
      this.defaultTabIndex = 1;
    }
    this._sharedService.deleteData('navCurPage');
    this._permission.checkPermissionObs('Create_Order').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
  }

  ngOnDestroy() {
    if (this.subcribleInWorkflowOrders !== undefined) {
      this.subcribleInWorkflowOrders.unsubscribe();
    }
    if (this.subcribleUnassignedOrders !== undefined) {
      this.subcribleUnassignedOrders.unsubscribe();
    }
  }

  enableSnackBar() {
    const orderMsg = this._sharedService.getData('orderCreated');
    if (orderMsg) {
      this._snackBarSvc.showSuccessSnackBar(orderMsg, SnackbarClasses.successBottom2);
      this._sharedService.deleteData('orderCreated');
    }
  }

  getUnassignedOrdersCount() {
    this.subcribleUnassignedOrders = this._service.getUnassignedOrdersCount().subscribe(res => {
      this.orderCount.unassigned = res;
    }, err => {
      console.log(err, 'error occured while fetching unassinged orders');
    });
  }

  getInworkflowOrdersCount() {
    this.subcribleInWorkflowOrders = this._service.getInworkflowOrdersCount().subscribe(res => {
      this.orderCount.inworkflow = res;
    }, err => {
      console.log(err, 'error occured while fetching inworkflow orders');
    });
  }

  getTabName(tabInfo: any) {
    if (this.orderCount) {
        const result =  this.translate.get(tabInfo.value, tabInfo.value);
        let respose;
        result.subscribe((res) => {
          if (this.orderCount[tabInfo.key] > 0) {
            res += ` (${this.orderCount[tabInfo.key]})`;
          }
          respose = res;
        });
        return respose;
      }
  }

  getCount(tabKey: any) {
    if (this.orderCount) {
      return this.orderCount[tabKey];
    }
  }

  goToCreateOrder() {
    this.router.navigate(['orders', 'create-order']);
  }

  onToggleFilterIcon(toggle){
    console.log("onToggleFilterIcon", this.unassignedFilter);
    this.toggleUnassignedFilter = toggle;
  }

  onToggleInworkflowFilterIcon(toggle){
    this.toggleInworkflowFilter = toggle;
  }

  filterCountUpdate(event){
    this.unassignedFilterCount = event;
  }

  inworkflowFilterCountUpdate(event){
    this.inworkflowFilterCount = event;
  }
}
