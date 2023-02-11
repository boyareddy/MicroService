import { Component, OnInit, ViewChild, AfterViewInit, AfterViewChecked } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { WorkflowService } from '../workflow.service';
import { ActivatedRoute, Router } from '@angular/router';
import { XhrProgressLoaderService } from '../../shared/xhr-progress-loader.service';
import { MatSort, MatTableDataSource, MatDialog } from '@angular/material';
import { SharedService } from '../../shared/shared.service';
import { ConfirmDialogMsgComponent } from 'src/app/shared/confirm-dialog-msg/confirm-dialog-msg.component';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { PermissionService } from 'src/app/shared/permission.service';
import { setLocal, MULTI_NAV, setLocalObject, clearLocal } from '../../shared/utils/local-storage.util';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-container-mapped-samples',
  templateUrl: './container-mapped-samples.component.html',
  styleUrls: ['./container-mapped-samples.component.scss']
})
export class ContainerMappedSamplesComponent implements OnInit, AfterViewChecked {

  @ViewChild(MatSort) sort: MatSort;
  public dataSourceNew: MatTableDataSource<SamplesInfo>;
  public listData: any;
  headerInfo: HeaderInfo = {
    headerName: 'Workflow',
    curPage: 'Pending NA extraction',
    isBackRequired: true,
    navigateUrl: 'workflow'
  };
  plateNo;
  containerId;
  displayedColumns: string[] = ['accessioningID', 'position', 'assayType', 'comments', 'goTo'];
  dataSource: MatTableDataSource<any>;
  havingAccess = false;
  queryParams;
  constructor(private _service: WorkflowService,
    private _acRoute: ActivatedRoute,
    private _route: Router,
    private _xhrLoaderSvc: XhrProgressLoaderService,
    private _sharedSvc: SharedService,
    private _dialogBox: MatDialog,
    private _snackBarSvc: SnackBarService,
    private _permission: PermissionService,
    private _translate: TranslateService
  ) { }

  ngOnInit() {
    // Clear the local cache for the order detail navigation.
    clearLocal(MULTI_NAV.ORDER_DETAIL_PREV);

    /* getting the query params  */
    this._acRoute.queryParams.subscribe(params => {
      this.queryParams = params;
      console.log(params['containerId'], params['plateNo']);
      this.plateNo = params['plateNo'];
      this.containerId = params['containerId'];
      if ((this.plateNo) && (this.containerId)
      ) {
        this.getMappedSamples(this.containerId);
      }
    });
    this._permission.checkPermissionObs('Delete_ContainerSamples').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
  }


  /**
   * getting the Mapped Samples from API
   * @param containerId
   */
  getMappedSamples(containerId) {
    this._xhrLoaderSvc.openProgress();
    this._service.getContainerMappedSamples(containerId).subscribe(response => {
      this._xhrLoaderSvc.closeProgress();
      this.listData = response;
      this.dataSource = new MatTableDataSource(this.listData);
      this.dataSource.sort = this.sort;
      // this.dataSource = response;
      console.log(response);
    }, error => {
      console.log('Error while getting container mapped samples', error);
    });
  }

  ngAfterViewChecked() {
    if (this.dataSource) {
      this.dataSource.sort = this.sort;
      this.dataSource.sortingDataAccessor = (data, header) => data[header];
    }
  }


  /**
   * by gathering the sample information naviate to order info page.
   * @param sampleInfo
   */
  goToOrderInfo(sampleInfo) {
    console.log(sampleInfo);
    if (sampleInfo.orderID) {
      sessionStorage.setItem('selectedItem', sampleInfo.orderID);
      setLocalObject(MULTI_NAV.ORDER_DETAIL_PREV, '/workflow/mapped-samples', this.queryParams);
      this._route.navigate(['orders', 'order-details', sampleInfo.orderID]);
    }
  }

  deleteContSamples(containerId) {
    const translations = this._translate.translations[this._translate.currentLang];
    console.log(containerId);
    // tslint:disable-next-line:max-line-length
    const confirmMessage = this._snackBarSvc.getMessage('workflowPage.confirmDeleteContainer').replace('$$$$', containerId);
    this._dialogBox.open(ConfirmDialogMsgComponent, {
      width: '600px',
      height: '190px',
      data: {param: confirmMessage, action: 'Delete', isWarn: true}
    }).afterClosed().subscribe(result => {
      if (result) {
        this._service.deleteContainerMappedSamples(containerId).subscribe(response => {
          const expResponse: any = response;
          const containerIdValOld = JSON.parse(expResponse);
          const containerIdVal = containerIdValOld ? containerIdValOld.values.toString() : '';
          const message = `${translations.runWorkflow.deleteMapping}`;
          const finalResponse = this._snackBarSvc.getMessage('runWorkflow.deleteMapping').replace('$$$', containerIdVal);
          this._sharedSvc.setData('mappingDeleted', finalResponse);
          this._route.navigate(['/workflow']);
        }, error => {
          console.log(error);
          const message = `${translations.runWorkflow.validations.connectivityIssue}`;
          this._snackBarSvc.showErrorSnackBar(message, 'failed-snackbar-bottom1');
        });
       }
    });
  }

}

export interface SamplesInfo {
  accessioningID: string;
  position: string;
  assayType: string;
  comments: string;
}
