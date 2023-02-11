import { Component, OnInit, OnChanges, Input, Output, EventEmitter, AfterViewChecked, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { Order, ORDER_TABLE_FIELDS } from '../../models/search.model';
import { Router } from '@angular/router';
import { SharedService } from '../../../services/shared.service';
import { MULTI_NAV, setLocal, setLocalObject, clearLocal } from '../../../utils/local-storage.util';

@Component({
  selector: 'app-order-table',
  templateUrl: './order-table.component.html',
  styleUrls: ['./order-table.component.scss']
})
export class OrderTableComponent implements OnInit, OnChanges, AfterViewInit, AfterViewChecked {

  dataSource: Order[];
  displayColumns: string[] = ORDER_TABLE_FIELDS;
  orderDetailsTab = null;
  isEnter: boolean = false;
  apiProps: any;
  selectedId = '';

  @Input() orders: Order[];
  @Input() searchQuery : number;
  @Input() isScrollXhrOnProgress: boolean = false;

  @Output() afterTableLoad: EventEmitter<boolean> = new EventEmitter(false);

  constructor(private _router: Router, private _sharedSV: SharedService) { 
    this.apiProps = this._sharedSV.getSharedData('appProperties');
  }

  ngOnInit() {
  }

  ngOnChanges(){
    this.dataSource = this.orders;
  }

  onNavigation(entity){
     // this.orderDetailsTab = window.open(`${this.apiProps.protocol}://${this.apiProps.host}:${this.apiProps.appPort}/connect_ui/#/orders/order-details/${entity.orderId}`, `myNewWindow${entity.orderId}`);
     window.open(`${this.apiProps.protocol}://${this.apiProps.host}:${this.apiProps.appPort}/connect_ui/#/orders/order-details/${entity.orderId}`, '_self')
  }

  ngAfterViewInit(){
    this.afterTableLoad.emit(true);
  }

  ngAfterViewChecked(){
    this.afterTableLoad.emit(true);
  }
}
