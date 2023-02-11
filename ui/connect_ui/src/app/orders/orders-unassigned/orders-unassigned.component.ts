import { Component, OnInit, Input, OnDestroy, ElementRef, AfterViewInit, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { OrderService } from '../order.service';
import { XhrProgressLoaderService } from 'src/app/shared/xhr-progress-loader.service';
import { detectChanges } from '@angular/core/src/render3';
import { FilterResult } from '../../shared/filter/filter.component';

@Component({
    selector: 'app-orders-unassigned',
    templateUrl: './orders-unassigned.component.html',
    styleUrls: ['./orders-unassigned.component.scss']
}

) export class OrdersUnassignedComponent implements OnInit, OnDestroy, AfterViewInit {
    dataSource: any = [];
    @Input() listCount: any;
    @Input() toggleUnassignedFilter: boolean = false;
    public unassignedOrders: any;
    subcribleUnassignedOrders;
    public isSprinerOn = false;
    public isResAvail = false;
    public rectPro;
    filterOption: any;

    @Output() filterCountUpdate: EventEmitter<any> = new EventEmitter();

    constructor(private router: Router,
                private translate: TranslateService,
                private _orderService: OrderService,
                private elem: ElementRef,
                private changeDetectorRef: ChangeDetectorRef
                ) {
                  this.onLoadingUnassignedOrderList();
                }

    ngOnInit() {
      setTimeout(() => {
        this.isSprinerOn = true;
      }, 500);
      setTimeout(() => {
        this.isSprinerOn = false;
      }, 3500);
    }

    ngOnDestroy() {
      if (this.subcribleUnassignedOrders !== undefined) {
        this.subcribleUnassignedOrders.unsubscribe();
      }
    }

    ngAfterViewInit() {
      setTimeout(() => {
        if (this.isSprinerOn) {
            console.log('htprun-card-main-container=');
            const rect = this.elem.nativeElement.querySelectorAll('.unassigned-container')[0].getBoundingClientRect();
            console.log(rect, 'rect');
            this.rectPro = rect;
        }
      }, 600);
    }

   onLoadingUnassignedOrderList() {
    this.subcribleUnassignedOrders = this._orderService.getUnassignedOrdersList().subscribe(resp => {
      this.isResAvail = true;
      this.isSprinerOn = false;
      this.dataSource = resp;
      // Sorting the response using pendding order status
      this.unassignedOrders = this.dataSource;
    }, error => {
      this.isSprinerOn = false;
      console.log('Error on unassigned getting Order List', error);
    });
  }

  onFilter(event: FilterResult) {
    this.filterOption = event.filterOptions;
    this.filterCountUpdate.emit(event.filterCount);
  }

  onFilterOptionCount(event){
    this.filterCountUpdate.emit(event);
  }
}
