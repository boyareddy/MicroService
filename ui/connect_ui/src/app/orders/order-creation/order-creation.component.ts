import { Component, OnInit } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { PermissionService } from 'src/app/shared/permission.service';

@Component({
  selector: 'app-order-creation',
  templateUrl: './order-creation.component.html',
  styleUrls: ['./order-creation.component.scss']
})
export class OrderCreationComponent implements OnInit {

  public headerInfo: HeaderInfo = {
    headerName: 'Create order',
    isCloseRequired: true,
    isCardsPage: true,
    navigateUrl: 'orders'
  };
  isRedirectingToUpload;
  havingAccess = false;
  constructor(
    private _permission: PermissionService
    ) { }

  ngOnInit() {
    this.isRedirectingToUpload = sessionStorage.getItem('isRedirectingToUpload');
    console.log('this.isRedirectingToUpload', this.isRedirectingToUpload);
    sessionStorage.removeItem('isRedirectingToUpload');
    this._permission.checkPermissionObs('Create_Bulk_Orders').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
  }

}
