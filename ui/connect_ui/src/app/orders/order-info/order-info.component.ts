import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { SharedService } from '../../shared/shared.service';
import { OrderService } from '../order.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MatSnackBar } from '@angular/material';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';
import { getHoursMin } from 'src/app/shared/functions/HoursSeconds';
import { getLocal, MULTI_NAV, getLocalObject } from '../../shared/utils/local-storage.util';
import { LowerCaseStatus, processStepNames, ConstantNames } from 'src/app/workflow/workflow-dashboard.util';
import { StatusIcons } from '../util/Constants';
import { WarningRequiredService } from 'src/app/shared/inline-htmls/warning-required.service';

@Component({
  selector: 'app-order-info',
  templateUrl: './order-info.component.html',
  styleUrls: ['./order-info.component.scss']
})
export class OrderInfoComponent implements OnInit, OnDestroy {

  orderInfo: any = [];
  orderInformation;
  assayDetails;
  patientInformation;
  isWorkFlowEnabled = true;
  public selectedIndex: number;
  headerInfo: HeaderInfo = {
    headerName: 'Orders',
    isBackRequired: true,
    removeSharedData: 'orderDetails',
    navigateUrl: 'orders'
  };
  oderId;
  assayTypeList;
  assayTypeId;
  workFlowSteps;
  workFlowDetails;
  workFlowDetails_Avail;
  resultManagementDetails;
  orderStatus;
  subcribleWorkFlowResults;
  assayType;
  public accessioningId: string;
  public completedImage = StatusIcons.RUN_COMPLETED;
  public abortedImage = StatusIcons.RUN_FAILED;
  public ongoingImage = StatusIcons.RUN_ONGOING;
  public processStepNameInfo = 'unassigned';
  public patientConfigInfo: any;

  constructor(
    private _sharedService: SharedService,
    private _orderService: OrderService,
    private _route: ActivatedRoute,
    private _snackBar: MatSnackBar,
    private _router: Router,
    private _snackBarSvc: SnackBarService,
    private _warningService: WarningRequiredService,
    private translate: TranslateService) {
      _router.events.subscribe((val) => {
        this.destroySub();
      });
    }

  ngOnInit() {
    window.scrollTo(0, 0);
    this._route.params.subscribe(params => {
      this.oderId = params['id'];
      sessionStorage.setItem('selectedItem', this.oderId);
      const workflowInfo = this._sharedService.getData('isWorkflowData');
      if (workflowInfo) {
        this.selectedIndex = 1;
        this._sharedService.deleteData('isWorkflowData');
      }
      this.getOrderDetails(this.oderId);
    });
    this.orderStatus = sessionStorage.getItem('orderStatus');
    // checking the order status
    if (this.orderStatus !== null && this.orderStatus !== undefined && this.orderStatus.indexOf('workflow') !== -1) {
      this.isWorkFlowEnabled = false;
    }

    const prevUrl: HeaderInfo = getLocalObject(MULTI_NAV.ORDER_DETAIL_PREV);
    this.headerInfo.navigateUrl = prevUrl.navigateUrl;
    this.headerInfo.queryParams = prevUrl.queryParams;
    // this.orderStatus = 'inworkflow';
    // this.isWorkFlowEnabled = false;
  }

  ngOnDestroy() {
   this.destroySub();
  }

  destroySub() {
    if (this.subcribleWorkFlowResults !== undefined) {
      this.subcribleWorkFlowResults.unsubscribe();
    }
    sessionStorage.removeItem('orderStatus');
  }

  goToProblemReport() {
    sessionStorage.setItem('accessioningId', this.accessioningId);
    const appProperties = this._sharedService.getData('appProperties');
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/admin_ui/#/settings`;
  }


  /**
   * Getting the order details by passing the order id to server.
   * Applying the response result to orderInfo variable.
   */
  public getOrderDetails(orderId): void {
    this._orderService.getOrderInfo(orderId).subscribe(response => {
      this._warningService.getLastProcessStepName();
      this.orderInfo = response;
      const accessioningID = this.translate.get('Accessioning ID', 'Accessioning ID');
      let accessioningIdName;
      accessioningID.subscribe((res) => {
        accessioningIdName = res;
      });
      this.accessioningId = this.orderInfo.order.accessioningId;
      this.headerInfo.curPage = `${accessioningIdName}: ${this.orderInfo.order.accessioningId}`;
      this.orderInformation = this.orderInfo.order;
      this.assayDetails = this.orderInfo.order;
      this.patientInformation = this.orderInfo.order.patient;
      this._sharedService.setData('orderDetails', this.orderInfo);
      this.orderStatus = this.orderInfo.order.orderStatus;
      this.getRequiredFieldsByAssay(this.orderInfo.order.assayType);
      this.assayType = this.orderInfo.order.assayType;
      // checking the order status
      if (this.orderInfo.order.orderStatus.indexOf('workflow') !== -1) {
        this.isWorkFlowEnabled = false;
        this.getWorkFlowSteps();
        this.getProcessStepByAccessioningId(this.orderInfo.order.accessioningId);
      }
    }, error => {
      this._snackBarSvc.showErrorSnackBar(`No order found with order ID : ${orderId}`, SnackbarClasses.errorBottom2);
      this._router.navigate(['orders']);
      console.log('Error on getting Order Info', error);
    });
  }

  /**
  * Getting Assay Details from API
  */
  onGettingAssayTypes() {
    this._orderService.getAssayTypes().subscribe(resp => {
      console.log(resp);
      this.assayTypeList = resp;
      if (this.orderInfo !== ''
        && this.orderInfo !== null &&
        this.orderInfo !== undefined) {
        // Getting the sample types for an seledted assay type
        this.getAssayTypeId(this.orderInfo.order.assayType);
        this.getWorkFlowSteps();
      }
    }, error => {
      console.log('Error on AssayTypes', error);
    });
  }

  /**
   * Getting the assay id by ussing the assay type name.
   */
  getAssayTypeId(assayType: string): number {
    this.assayTypeList.filter((obj) => {
      if (assayType === obj.assayType) {
        this.assayTypeId = obj.assayTypeId;
        return this.assayTypeId;
      }
    });
    return this.assayTypeId;
  }

  /**
  * Getting workflow steps from amm API
  */
  getWorkFlowSteps() {
    this._orderService.getWorkFlowSteps(this.orderInfo.order.assayType).subscribe(resp => {
      this.workFlowSteps = resp;
      this.getWorkFlowResults();
    });
  }

  /**
  * Getting workflow results from wff api
  */
  getWorkFlowResults() {
    this.subcribleWorkFlowResults = this._orderService.getWorkFlowResults(this.orderInfo.order.accessioningId).subscribe(resp => {
      this.resultManagementDetails = resp;
      this.createJsonForWorkFlow();
    });
  }

  /**
   * Creating json for work flow page
   * From the result of AMM and WFF
   */
  createJsonForWorkFlow() {
    this.workFlowDetails = [];
    let isExpandEnabled = false;
    let resIndex = null;
    if (this.workFlowSteps !== null && this.workFlowSteps !== undefined && this.workFlowSteps !== '') {
      this.workFlowSteps.forEach((value, index) => {
        this.workFlowDetails[index] = {};
        this.workFlowDetails[index]['processStepName'] = value.processStepName;
        this.workFlowDetails[index]['assayType'] = this.orderInfo.order.assayType;
        if (this.resultManagementDetails !== null && this.resultManagementDetails !== undefined && this.resultManagementDetails !== '') {
          this.resultManagementDetails.forEach((reValue, _resIndex) => {
            // tslint:disable-next-line:max-line-length
            if (reValue.processStepName !== null && (value.processStepName.toLowerCase() === reValue.processStepName.toLowerCase())) {
              // tslint:disable-next-line:max-line-length
              if (reValue.runStatus.toLowerCase().indexOf('inprogress') !== -1 || reValue.runStatus.toLowerCase().indexOf('inprocess') !== -1 || reValue.runStatus.toLowerCase().indexOf('started') !== -1) {
                this.workFlowDetails[index]['status'] = 'Ongoing';
                this.workFlowDetails[index]['statusImage'] = this.ongoingImage;
                let filterProtocol = [];
                if (reValue.protocolName !== null && reValue.protocolName !== undefined && reValue.protocolName.length > 0) {
                  // filterProtocol = [];
                  filterProtocol = Array.from(new Set(reValue.protocolName));
                }
                this.workFlowDetails[index]['runProtocol'] = filterProtocol;
                this.workFlowDetails[index]['runStartedAt'] = reValue.runStartTime;
                this.workFlowDetails[index]['runRemainingTime'] = getHoursMin(reValue.runRemainingTime);
                this.workFlowDetails[index]['isOpenOnLoad'] = true;
                this.workFlowDetails[index]['runstartedBy'] = reValue.operatorName;

                if (value.processStepName === processStepNames.Sequencing) {
                  let findResult;
                  const { runResultsDetailDTO } = reValue;
                  if (runResultsDetailDTO && runResultsDetailDTO.length > 0) {
                    findResult = runResultsDetailDTO.find(ele => ele.attributeName === ConstantNames.ComplexId);
                  }
                  if (findResult) {
                    this.workFlowDetails[index]['poolTubeId'] = findResult.attributeValue;
                  }
                }

                isExpandEnabled = true;
              }
              if ((reValue.runStatus.toLowerCase().indexOf('completed') !== -1) ||
              (reValue.runStatus.toLowerCase().indexOf('aborted') !== -1) ||
              (reValue.runStatus.toLowerCase().indexOf('failed') !== -1)) {
                if (reValue.runStatus.toLowerCase() === 'failed') {
                  this.workFlowDetails[index]['status'] = 'Failed';
                  this.workFlowDetails[index]['statusImage'] = this.abortedImage;
                } else if (reValue.runStatus.toLowerCase() === 'aborted') {
                  this.workFlowDetails[index]['status'] = 'Aborted';
                  this.workFlowDetails[index]['statusImage'] = this.abortedImage;
                } else if (reValue.runStatus.toLowerCase() === 'completed') {
                  this.workFlowDetails[index]['status'] = 'Completed';
                  this.workFlowDetails[index]['statusImage'] = this.completedImage;
                }
                this.workFlowDetails[index]['containerType'] = reValue.outputContainerType;
                this.workFlowDetails[index]['containerId'] = reValue.outputContainerId;
                this.workFlowDetails[index]['flag'] = reValue.runFlag;
                this.workFlowDetails[index]['runstartedBy'] = reValue.operatorName;
                this.workFlowDetails[index]['runcompletionTime'] = reValue.runCompletionTime;
                let filterProtocol = null;
                if (reValue.protocolName !== null && reValue.protocolName !== undefined && reValue.protocolName.length > 0) {
                  // filterProtocol = [];
                  filterProtocol = Array.from(new Set(reValue.protocolName));
                }
                this.workFlowDetails[index]['runProtocol'] = filterProtocol;
                this.workFlowDetails[index]['position'] = reValue.outputContainerPosition;
                this.workFlowDetails[index]['comments'] = reValue.comments;
                this.workFlowDetails[index]['plateType'] = reValue.plateType;
                this.workFlowDetails[index]['plateLocation'] = reValue.outputContainerPosition;
                this.workFlowDetails[index]['laneNumber'] = reValue.outputContainerPosition;
                this.workFlowDetails[index]['numberOfCycles'] = reValue.numberOfCycles;
                if (reValue.runResultsDetailDTO && reValue.runResultsDetailDTO.length > 0) {
                  reValue.runResultsDetailDTO.forEach((reValue_, _resIndex_) => {
                    const assayType = this.orderInfo.order.assayType;
                    if (assayType === 'NIPTHTP' && reValue_.attributeName === 'output_folder') {
                      this.workFlowDetails[index]['outputLocation'] = reValue_.attributeValue;
                    } else if (assayType === 'NIPTDPCR' && reValue_.attributeName.toLowerCase() === 'dpcr analyzer filepath') {
                      this.workFlowDetails[index]['outputLocation'] = reValue_.attributeValue;
                    }
                  });

                  const position = reValue.runResultsDetailDTO.find(ele => ele.attributeName === ConstantNames.Lane_No);
                  if (position) {
                    this.workFlowDetails[index]['laneNo'] = position.attributeValue;
                  } else {
                    this.workFlowDetails[index]['laneNo'] = null;
                  }

                }

                if (value.processStepName === processStepNames.Sequencing) {
                  let findReagent;
                  const { uniqueReagentsAndConsumables: Reagents } = reValue;
                  console.log(Reagents, 'sequencing reagents');
                  if (Reagents && Reagents.length > 0) {
                    findReagent = Reagents.find(ele => ele.attributeName === ConstantNames.Consumable_Device_Part_Number);
                  }
                  if (findReagent) {
                    this.workFlowDetails[index]['consumableDeviceId'] = findReagent.attributeValue;
                  }
                }

                if (reValue.sampleResultsDetailDTO && reValue.sampleResultsDetailDTO.length > 0) {
                  reValue.sampleResultsDetailDTO.forEach((key: any) => {
                    if (key.attributeName === 'qualitativeValue') {
                      this.workFlowDetails[index]['qualitativeResult'] = key.attributeValue;
                    }
                    if (key.attributeName === 'quantitativeValue') {
                      this.workFlowDetails[index]['quantitativeResult'] = key.attributeValue;
                    }
                  });
                }
              }

              this.workFlowDetails[index]['runResultId'] = reValue.runResultId;
              console.log(this.workFlowDetails);
            }
          });
        }
      });
    }
    if (!isExpandEnabled) {
      if (this.workFlowDetails !== null && this.workFlowDetails !== undefined && this.workFlowDetails !== '') {
        this.workFlowDetails.forEach((value, indx) => {
          if (value.status !== null && value.status !== undefined &&
            (value.status.toLowerCase().indexOf('completed') !== -1 ||
            value.status.toLowerCase().indexOf('aborted') !== -1 ||
            value.status.toLowerCase().indexOf('failed') !== -1)) {
            resIndex = indx;
          }
        });
        if (resIndex != null && resIndex !== undefined) {
          this.workFlowDetails[resIndex]['isOpenOnLoad'] = true;
        }
      }
    }
    if (this.workFlowDetails !== null && this.workFlowDetails !== undefined && this.workFlowDetails !== '') {
      // tslint:disable-next-line:max-line-length
      if (this.workFlowDetails_Avail !== null && this.workFlowDetails_Avail !== undefined && this.workFlowDetails_Avail !== '') {
        if (JSON.stringify(this.workFlowDetails_Avail).toLowerCase() !== JSON.stringify(this.workFlowDetails).toLowerCase()) {
          this.workFlowDetails_Avail = this.workFlowDetails;
        }
      } else {
        this.workFlowDetails_Avail = this.workFlowDetails;
      }
    }
  }

  getRequiredFieldsByAssay(assayType: string) {
    this._orderService.getRequiredFieldsByAssay(assayType).subscribe(response => {
      this.patientConfigInfo = response;
    }, error => {
      console.log(error);
    });
  }

  getProcessStepByAccessioningId(accessioningID: string) {
    this._orderService.getProcessStepByAccessioningId(accessioningID).subscribe(response => {
      this.processStepNameInfo = response[0].workflowType;
    }, error => {
      console.log(error);
    });
  }

}

