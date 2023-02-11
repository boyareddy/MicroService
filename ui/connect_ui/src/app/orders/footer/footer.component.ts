import { Component, OnInit, Input, OnChanges, EventEmitter, OnDestroy } from '@angular/core';
import { OrderService } from '../order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit, OnChanges, OnDestroy {

  selectedListItem;
  curentIndex;
  index;
  allRecords;
  @Input()
  orderStatus;
  subcribleInWorkflowOrders;
  subcribleUnassignedOrders;

  constructor(
    private orderSer: OrderService,
    private _router: Router,
  ) { }

  ngOnInit() {
  }

  ngOnChanges() {
    this.getOrderList();
  }

  ngOnDestroy() {
    this.destroyTheSubc();
  }

  public destroyTheSubc() {
    if (this.subcribleInWorkflowOrders !== undefined) {
      this.subcribleInWorkflowOrders.unsubscribe();
    }
    if (this.subcribleUnassignedOrders !== undefined) {
      this.subcribleUnassignedOrders.unsubscribe();
    }
  }

  getOrderList() {
    if (this.orderStatus && this.orderStatus.toLowerCase() === 'inworkflow') {
      this.subcribleInWorkflowOrders = this.orderSer.getInWorkflowOrdersList().subscribe(response => {
        this.setCurrentIndex(response);
      }, error => {
        console.log('Error on GettingInWorkflowOrdersList', error);
      });
    } else if (this.orderStatus && this.orderStatus.toLowerCase() === 'unassigned') {
      this.subcribleUnassignedOrders = this.orderSer.getUnassignedOrdersList().subscribe(response => {
        this.setCurrentIndex(response);
      }, error => {
        console.log('Error on getUnassignedOrdersList', error);
      });
    }
  }

  /**
   * Setting the response and find the current index
   */
  setCurrentIndex(response) {
    // tslint:disable-next-line:radix
    const localStorageValue: number = parseInt(sessionStorage.getItem('selectedItem'));
    this.allRecords = response;
    this.allRecords.find((item, i) => {
      if (item.orderId === localStorageValue) {
        this.index = i;
        return i;
      }
    });
  }

  /**
   * Find the next order id, by using the seledted order id.
   * Getting the next and previous order details.
   */
  findPrevNextOrders(condition) {
    if (condition === 'prev') {
      this.index--;
    } else {
      this.index++;
    }
    this.destroyTheSubc();
    sessionStorage.setItem('selectedItem', this.allRecords[this.index].orderId);
    this._router.navigate(['orders', 'order-details', this.allRecords[this.index].orderId]);
  }
}
