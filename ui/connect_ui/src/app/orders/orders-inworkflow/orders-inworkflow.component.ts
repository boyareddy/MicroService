import { Component, OnInit, Input, ViewChild, OnDestroy, AfterViewChecked, ElementRef, AfterViewInit, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { SharedService } from '../../shared/shared.service';
import { MatTableDataSource, MatSort } from '@angular/material';
import { OrderService } from '../order.service';
import { XhrProgressLoaderService } from '../../shared/xhr-progress-loader.service';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { MULTI_NAV, setLocal, setLocalObject, clearLocal } from '../../shared/utils/local-storage.util';
import { FilterResult } from '../../shared/filter/filter.component';
import { FilterPipe } from '../../shared/filter/filter.pipe';
import { MatIconInfoService } from 'src/app/shared/mat-icons/mat-icon-info.service';

@Component({
  selector: 'app-orders-inworkflow',
  templateUrl: './orders-inworkflow.component.html',
  styleUrls: ['./orders-inworkflow.component.scss']
})
export class OrdersInworkflowComponent implements OnInit, AfterViewChecked, OnDestroy, AfterViewInit {

  inworkflowOrders: MatTableDataSource<any>;
  inworkflowOrderList: any;
  @ViewChild(MatSort) sort: MatSort;
  @Input() listCount;
  displayedColumns: string[] = ['accessioningId', 'assaytype', 'sampletype', 'workflowType', 'flags', 'priority', 'comments', 'symbol'];
  selectedId = '';
  subcribleInWorkflowOrders;
  public isSprinerOn = false;
  public rectPro;
  filterOption: any;

  @Input() toggleInworkflowFilter: boolean = false;

  @Output() filterCountUpdate: EventEmitter<any> = new EventEmitter();

  constructor(
    private _orderService: OrderService,
    private _router: Router,
    private _sharedService: SharedService,
    private _snackBarSvc: SnackBarService,
    private elem: ElementRef,
    private iconsService: MatIconInfoService
) {
  this.iconsService.initIcons();
  this.onGettingInWorkflowOrdersList();
}

  ngOnInit() {
    this.isSprinerOn = true;
    // this.iconsService.initIcons();
    clearLocal(MULTI_NAV.ORDER_DETAIL_PREV);
  }

  ngOnDestroy() {
    if (this.subcribleInWorkflowOrders !== undefined) {
      this.subcribleInWorkflowOrders.unsubscribe();
    }
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if (this.isSprinerOn) {
         const rect = this.elem.nativeElement.querySelectorAll('.inworkflow-orderlist')[0].getBoundingClientRect();
         this.rectPro = rect;
     }
    }, 600);
  }

  ngAfterViewChecked() {
    if (this.inworkflowOrders) {
      this.inworkflowOrders.sort = this.sort;
      this.inworkflowOrders.sortingDataAccessor = (data, header) => data[header];
    }
  }

  /**
   * onGettingInWorkflowOrdersList retrieves the data from getInWorkFlow in service worker OrderApiService
   */
  public onGettingInWorkflowOrdersList(): void {
    this.subcribleInWorkflowOrders = this._orderService.getInWorkflowOrdersList().subscribe((result: any) => {
      this.isSprinerOn = false;
      // console.log(result, 'result');
      const data = result;
      this.inworkflowOrderList = data;
      this.inworkflowOrders = new MatTableDataSource(data);
      /* this.inworkflowOrders = result; */
      this.inworkflowOrders.sort = this.sort;
    }, error => {
      this.isSprinerOn = false;
      console.log('Error on GettingInWorkflowOrdersList', error);
    });
  }


  /**
     * Setting the selected item information in sessionStorage.
     * navigating to order details page.
     */
  showSelectedItem(element) {
    this.selectedId = element.accessioningId;
    sessionStorage.setItem('selectedItem', element.orderId);
    sessionStorage.setItem('orderStatus', 'inworkflow');
    setLocalObject(MULTI_NAV.ORDER_DETAIL_PREV, this._router.url);
    this._sharedService.setData('navCurPage', 'In Workflow');
    this._router.navigate(['orders', 'order-details', element.orderId]);
  }

  response(event) {
    console.log(event);
    if (typeof event === 'object') {
      this._orderService.postComments(event).subscribe(res => {
        console.log(res);
        if (this.subcribleInWorkflowOrders !== undefined) {
          this.subcribleInWorkflowOrders.unsubscribe();
        }
          this.onGettingInWorkflowOrdersList();
      }, error => {
          this._snackBarSvc.showErrorSnackBar('Error Occured updating comments', 'failed-snackbar-bottom2');
      });
  }
  }

  isEmptyFlag (value) {
    if (value === '' || !value) {
      return true;
    } else {
      return false;
    }
  }

  update(el: any, comment: string) {
    if (comment == null) { return; }
    // copy and mutate
    console.log(el, comment);
    const event = {
      accesssioningId: el.accessioningId,
      comments: comment,
      processStepName: el.workflowType
    };
    console.log(event);
    this._orderService.postComments(event).subscribe(res => {
      if (this.subcribleInWorkflowOrders !== undefined) {
        this.subcribleInWorkflowOrders.unsubscribe();
      }
      this.onGettingInWorkflowOrdersList();
    }, error => {
      console.error(error, 'error occured from API');
      this._snackBarSvc.showErrorSnackBar('Error Occured updating comments', 'failed-snackbar-bottom2');
    });
  }

  onFilter(event: FilterResult) {
    let filterPipe: FilterPipe = new FilterPipe();
    let filteredResult;
    this.filterOption = event.filterOptions;
    filteredResult = filterPipe.getFilteredList(this.inworkflowOrderList, this.filterOption, 'createDate', 'comments', 'flags','mandatoryFieldMissing');
    this.inworkflowOrders = new MatTableDataSource(filteredResult);
    this.inworkflowOrders.sort = this.sort;
    this.filterCountUpdate.emit(event.filterCount);
  }

  onFilterOptionCount(event){
    this.filterCountUpdate.emit(event);
  }
}
