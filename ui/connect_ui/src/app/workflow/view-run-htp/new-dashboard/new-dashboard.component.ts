import { Component, OnInit, OnDestroy, AfterViewInit, ElementRef } from '@angular/core';
import { RunCardQueryParam, STATUS_CODES } from '../../model';
import { WorkflowService } from '../../workflow.service';
import { HeaderInfo } from 'src/app/shared/header.model';
import { Router } from '@angular/router';
import { getCss, labelKeys, imgPath, BasicAssayType, ConstantNames,
         LowerCaseStepNames, LowerCaseStatus, processStepDetails,
        htpLPSteps, readOutSteps, completedTabInfo, processStepNames, UpdatedlabelKeys, getUpdatedPRocessStepName } from '../../workflow-dashboard.util';
import { TranslateService } from '@ngx-translate/core';
import { PermissionService } from 'src/app/shared/permission.service';
import { SharedService } from 'src/app/shared/shared.service';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';
import { FilterResult } from '../../../shared/filter/filter.component';

@Component({
  selector: 'app-new-dashboard',
  templateUrl: './new-dashboard.component.html',
  styleUrls: ['./new-dashboard.component.scss']
})
export class NewDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  constructor(
    private _ws: WorkflowService,
    private _translate: TranslateService,
    private _router: Router,
    private _elementRef: ElementRef,
    private _sharedService: SharedService,
    private _snackBarSvc: SnackBarService,
    private _permission: PermissionService
  ) { }
  public assayType: any;
  public xhrCounter = 0;
  public totalXhrCount = 0;
  public runCardDetiils = [];
  public finalResponse = [];
  public count = 0;
  public labelNames: any = processStepDetails;
  public objectKeys = Object.keys;
  public subscribeXHRInfo = [];
  public isSprinerOn = false;
  public rectPro: any;
  oldcount = 1;
  public isRunCompletedAvail = false;
  public headerInfo: HeaderInfo = {
    headerName: 'Workflow manager',
    curPage: 'Workflow',
    removeSharedData: 'viewHTPMRunInfo',
    headerIcon: 'assets/Images/header/module/Workflow.svg'
  };

  completedTab = completedTabInfo;
  lpProcessNames = ConstantNames.Library_Preparation;
  completedtabList = [];
  niptHtpLpNames = htpLPSteps;
  niptMiddleNames = readOutSteps;
  public havingAccess = false;
  runDetails = [];
  filterOption: any;
  filterOptionCount = 0;
  selectedIndex = 0;
  previousPage;
  /**
   * OnInit method is the inherited method.
   */
  ngOnInit() {
    /* clearing the completed tab list run details and label names if assaytype changes */
    this.completedtabList = [];
    this.runDetails = [];

    this.previousPage = sessionStorage.getItem('selectedRow');
    console.log(this.previousPage, 'previous');
    if (this.previousPage && this.previousPage === 'Completed tab') {
      this.selectedIndex = 1;
      sessionStorage.removeItem('selectedRow');
    }

    /* unsubscribing the XHR calls */
    this._unsubscribeXHRCalls();

    /* Snackbar services */
    const isSnackBarShown = this._sharedService.getData('csvUploadSuccess');
    const mappingDeleted = this._sharedService.getData('mappingDeleted');
    const runError = this._sharedService.getData('runDetailsError');
    if (this._sharedService.getData('csvUploadSuccess')) {
      this._snackBarSvc.showSuccessSnackBar(isSnackBarShown, SnackbarClasses.successBottom2);
      this._sharedService.deleteData('csvUploadSuccess');
    } else if (mappingDeleted) {
      this._snackBarSvc.showSuccessSnackBar(mappingDeleted, SnackbarClasses.successBottom2);
      this._sharedService.deleteData('mappingDeleted');
    } else if (runError) {
      this._snackBarSvc.showSuccessSnackBar(runError, SnackbarClasses.errorBottom2);
      this._sharedService.deleteData('runDetailsError');
    }
    /* permissions for user */
    this._permission.checkPermissionObs('Validate_Container_Samples').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });

    this._loadProcessStepName();
  }


  ngAfterViewInit() {
    /* progress bar onload level */
    if (this.isSprinerOn) {
        // console.log('htprun-card-main-container=');
        const rect = this._elementRef.nativeElement.querySelectorAll('.run-info-container-ongoing')[0].getBoundingClientRect();
        // console.log(rect, 'rect');
        this.rectPro = rect;
    }
}

  ngOnDestroy() {
    /* unsubscribing the XHR calss */
    this._unsubscribeXHRCalls();
    this.isSprinerOn = false;
  }

  /**
   * unsubscribing the XHR calls on destroy level or query params change
   */
  public _unsubscribeXHRCalls() {
    if (this.subscribeXHRInfo.length > 0) {
      // console.log(this.subscribeXHRInfo.length, 'before');
      this.subscribeXHRInfo.forEach(ele => ele.unsubscribe());
      this.subscribeXHRInfo = [];
    }
  }

  /**
   * Fetch the Porcess step names
   * @param assayType assayType
   */
  public _loadProcessStepName() {
    this.totalXhrCount = 30;
    this.isSprinerOn = true;
    const assayTypes = ['NIPTHTP', 'NIPTDPCR'];
    const stepNames = [];
    assayTypes.forEach(assay => {
      this._ws.getWorkFlowSteps(assay).subscribe(
        response => {
          const labelResponse = <any>response;
          const labels = labelResponse.map(ele => ele.processStepName);
          stepNames.push(labels);
          this._loadRunCards(response, assay);
        },
        error => {
          this.isSprinerOn = false;
          console.error(error);
        }
      );
    });
    if (stepNames.length > 0) {
      this.totalXhrCount = stepNames.length * 3;
      this.labelNames = ['NA Extraction', 'Library Preparation', 'Read out', 'Analysis'];
    }
  }

  /**
   * process stepname based it call XHR services.
   * @param processSteps processStepNames
   */
  public _loadRunCards(processSteps: any, assay: string) {
    /* XHR API calls */
    for (let index = 0; index < processSteps.length; index++) {
      const processStepName = processSteps[index].processStepName;

      /* Create a completed XHR call queryparams */
      const completedQuery: RunCardQueryParam = {
        assayTypeDetail: assay,
        processStepVal: processStepName
      };

      /* Create a Inprogress XHR call queryparams */
      const InprogressQuery: RunCardQueryParam = {
        ...completedQuery,
        statusVal: STATUS_CODES.IN_PROGRESS
      };

      /* Create a aborted XHR call queryparams */
      const abortedQuery: RunCardQueryParam = {
        ...completedQuery,
        statusVal: STATUS_CODES.ABORTED
      };

      /* Create a failed XHR call queryparams */
      const failedQuery: RunCardQueryParam = {
        ...completedQuery,
        statusVal: STATUS_CODES.FAILED
      };

      /* call the XHR call for completed records */
      this._getCompletedRecords(completedQuery);

      /* XHR call for failed/aborted based on condition */
      if (
        (assay === BasicAssayType.NIPTHTP &&
          processStepName.toLowerCase() === LowerCaseStepNames.sequencing) ||
        (assay === BasicAssayType.NIPTDPCR &&
          processStepName.toLowerCase() === LowerCaseStepNames.na_extraction) ||
          (assay === BasicAssayType.NIPTDPCR &&
            processStepName.toLowerCase() === LowerCaseStepNames.library_preparation)
      ) {
        this._getInprogressAbortedRecords(failedQuery);
      } else {
        this._getInprogressAbortedRecords(abortedQuery);
      }

      /* XHR call for ongoing/pending card based on condition */
      if (
        !(
          assay === BasicAssayType.NIPTDPCR &&
          processStepName.toLowerCase() === LowerCaseStepNames.na_extraction
        )
      ) {
        this._getInprogressAbortedRecords(InprogressQuery);
      } else {
        const statusKey = ConstantNames.Na_Extraction_Open_Status_key;
        this._getNAExtractionOpenRecords(statusKey, processStepName);
      }
    }
  }

  /**
   * fetch XHR response for NA Extraction
   * @param key status open,sendtodevice
   * @param stepName processstepname
   */
  public _getNAExtractionOpenRecords(key: any, stepName: string) {
    // console.error(this.oldcount++, stepName, 'open status');
    /* initialise the XHR request subscription */
    let NAExtractionXHRRequest = null;
    /* XHR call subscription for NA Extraction pending */
    NAExtractionXHRRequest = this._ws.getNAextractionInfo(key).subscribe(
      response => {
        this.xhrCounter++;
        // // console.log(response);
        const newResponse = <any>response;

        /* Adding process stepname details */
        newResponse.forEach((element: any) => {
          element['processStepName'] = stepName;
          element['assayType'] = BasicAssayType.NIPTDPCR;
          element['runStatus'] = 'Pending';
          element['totalSamplecount'] = element.sampleCount;
          element['deviceRunId'] = element.devicerunid;
        });
        // // console.log(newResponse);
        /* Passing the response to this method */
        this._onWaitFinalResponse(response);
      },
      error => {
        this.xhrCounter++;
        console.error(error);
      }
    );
    this.subscribeXHRInfo[this.subscribeXHRInfo.length] = NAExtractionXHRRequest;
  }

  /**
   * fetch the Aborted/Failed/Inprogress record XHR response
   * @param inputInfo inputData for aborted/failed/Inprogress status
   */
  public _getInprogressAbortedRecords(inputInfo: RunCardQueryParam) {
    // console.error(this.oldcount++, inputInfo.processStepVal, 'inab');
    /* initialise the XHR request subscription */
    let XHRResponse = null;
    /* XHR call subscription for Ongoing/Failed/Aborted */
    XHRResponse = this._ws.getRunInfoByWFS(inputInfo).subscribe(
      responseInAb => {
        this.xhrCounter++;
        /* Passing the response to this method */
        this._onWaitFinalResponse(responseInAb);
      },
      errorIpAb => {
        this.xhrCounter++;
        this._onWaitFinalResponse([]);
        console.error(errorIpAb);
      }
    );
    /* Subscribing the XHR Information */
    this.subscribeXHRInfo[this.subscribeXHRInfo.length] = XHRResponse;
  }

  /**
   * fetch all completed records XHR call
   * @param inputInfo inputdata form completed records
   */
  public _getCompletedRecords(inputInfo: RunCardQueryParam) {
    // console.error(this.oldcount++, inputInfo.processStepVal, 'compl');
    /* initialise the XHR request subscription */
    let completedXHRResponse = null;
    /* XHR call subscription for Completed */
    completedXHRResponse = this._ws.getRunInfoInCompletedByWFS(inputInfo).subscribe(
      responseCompleted => {
        this.xhrCounter++;
         /* Passing the response to this method */
        this._onWaitFinalResponse(responseCompleted);
      },
      errorCompleted => {
        this.xhrCounter++;
        this._onWaitFinalResponse([]);
        console.error(errorCompleted);
      }
    );
    /* Subscribing the XHR Information */
    this.subscribeXHRInfo[this.subscribeXHRInfo.length] = completedXHRResponse;
  }

  /**
   * it holds the response untill get the response from last XHR call
   * @param records XHR data information
   */
  public _onWaitFinalResponse(records: any) {
    // console.log(this.xhrCounter, 'counter');
    /* XHR Information is available concat to finalresponse */
    if (records && records.length > 0) {
      // console.log(this.finalResponse);
      // console.log(typeof records);
      this.finalResponse = this.finalResponse.concat(Array.from(records));
      // console.log(this.finalResponse);
    }
    // console.error('xhr', this.xhrCounter, 'total', this.totalXhrCount);
    /* XHR count is equal to process step count is equal then call to _createResponse method */
    if (this.xhrCounter === this.totalXhrCount) {
      this._createResponse(this.finalResponse);
    }
  }

  /**
   * Hold all the XHR Information and segregate the data based on ProcessStepname
   * @param finalResponse allXHR data information
   */
  public _createResponse(finalResponse: any) {
    // console.log(this.xhrCounter, 'counter');
    // console.log(finalResponse);

    /* Resetting the XHR counter and completed tabList and runDetails Info if available (for auto refresh level) */
    this.xhrCounter = 0;
    this.completedtabList = [];
    this.runDetails = [];
    /* Calling the method once final response updated */
    this._allRecordsInfo(finalResponse);
    this.finalResponse = [];
  }


  /**
   * it will filter and sort the data based on updated date and time
   * @param record XHR information
   */
  public _allRecordsInfo(record: any) {
    /* getting completed/ongoing/pending details on Object destructing */
    const { completed, ongoing, pending } = LowerCaseStatus;

    /* Sorting the records if updatedDateTime is available or default it will come*/
    /* iterate the each element for filter the details and assign to corresponding process step name */
    record
      .sort((a: any, b: any) =>
        a.updatedDateTime > b.updatedDateTime
          ? -1
          : a.updatedDateTime < b.updatedDateTime
          ? 1
          : 0
      )
      .forEach(element => {
        // console.log(element, 'workflow records');
        /* holding the run status of the record */
        const status = element.runStatus.toLowerCase();

        /* fetching the wfmsflag details */
        const wfmsflag = element.wfmsflag
          ? element.wfmsflag.toLowerCase()
          : element.wfmsflag;

        /* checking the wfms flag information for showing the data in which tab Ongoing/Completed */
        if (
          (status.indexOf(completed) > -1 && element.wfmsflag.toLowerCase() === ongoing) ||
          (status.indexOf(completed) > -1 && element.wfmsflag.toLowerCase() === pending) ||
          status.indexOf(completed) === -1 || !wfmsflag
        ) {
          /* create a variable to holidng the filtered record information */
          let filterdRecords: any;

          /* checking if wfms flag is pending means leftover samples scenario otherwise show card*/
          if (wfmsflag && (wfmsflag.toLowerCase() === pending)) {
            /* Pending Code for left over card scenario*/
            this._pendingRunCardMapper(element);
          } else {
            /* filters the record information remove unused attribute names and values  */
            filterdRecords = this._runCardmapper(element);
          }

          /* Create the process stepname for HTP and DPCR as required format */
          const processStepName =
            this.niptHtpLpNames.indexOf(element.processStepName) > -1
              ? this.lpProcessNames
              : this.niptMiddleNames.indexOf(element.processStepName) > -1 ? processStepNames.Read_Out : element.processStepName;

          /* Assign the record to corresponding process stepname */
          this.runDetails[processStepName]
            ? this.runDetails[processStepName].push(filterdRecords)
            : (this.runDetails[processStepName] = [filterdRecords]);
            // console.log(this.runDetails, 'before');
        } else if (this.completedTab.indexOf(wfmsflag) > -1) {
          /* Completed tab records information */
          this.completedtabList.push(element);
          // console.log(this.runDetails, 'after');
        }
        // console.log(this.runDetails, 'final');
      });
    /* Once records is filtered and processed then progressbar will stop */
    this.isSprinerOn = false;
    console.log('completedtabList', this.completedtabList);
  }

  /**
   * it will filetr the XHR information
   * @param runCardResponse cards details
   */
  public _runCardmapper(runCardResponse: any) {

    /* getting completed/ongoing/pending/failed/inprogress details on Object destructing for LowerCaseStatus*/
    const { completed, aborted, failed, pending, inprogress } = LowerCaseStatus;

    /* Filtered card Info */
    let finalProcessedCard: any;
    /* Using Object destructing  filter the common required details*/
    // tslint:disable-next-line:prefer-const
    let card = (({
      totalSamplecount, deviceRunId, runStatus, processStepName, totalSampleFlagCount, totalSampleFailedCount,
      deviceId, runResultId, assayType
    }) => ({
      totalSamplecount, deviceRunId, runStatus, processStepName, totalSampleFlagCount, totalSampleFailedCount,
      deviceId, runResultId, assayType
    }))(runCardResponse);

    const { wfmsflag } = runCardResponse;
    let flag;
    if (card.totalSamplecount > 0 && runCardResponse.sampleResults) {
      const { sampleResults } = runCardResponse;
      flag = sampleResults.map(ele => ele.flag).join();
    }

    card['flag'] = flag;

    // const { output container Ids } = runCardResponse;
    /* outputcontainer id details for particular card */
    const outputcontainerId = (({ outputcontainerIds }) => ({
      outputcontainerIds
    }))(runCardResponse);
    /* inputcontainer id details for particular card */
    const inputcontainerId = (({ inputcontainerIds }) => ({
      inputcontainerIds
    }))(runCardResponse);
    // const { inputcontainerIds } = runCardResponse;

    const status = card.runStatus.toLowerCase();

    /* fetch the card color code of card by passed the run status */
    const colorCode = getCss(status);

    /* fetch the image path of card by passing the process step name and runcard status */
    const imagePath = imgPath(card.processStepName, card.runStatus, card.assayType);

    /* fetch the label name by passing the process stepname and status and assaytype and wfmsflag */
    const labelName = labelKeys(
      card.processStepName,
      card.runStatus,
      card.assayType,
      wfmsflag
    );
    // // console.log(labelName, 'labelName');
    // // console.log(colorCode);


    const notInprogressStatus =
        (status.indexOf(completed) > -1 || status.indexOf(aborted) > -1 || status.indexOf(failed) > -1) &&
        card.processStepName !== processStepNames.DPCR;

      const workflowFlagStatus = wfmsflag === pending;

      // let outputContainerIdsLength: number;
      let updatedCode;
      if (notInprogressStatus || workflowFlagStatus) {
          if (outputcontainerId &&  outputcontainerId.outputcontainerIds) {
            const outputIds = outputcontainerId.outputcontainerIds;
            const result = outputIds.filter(ele => ele.stripId !== null);
            const length = result.length;
            updatedCode = UpdatedlabelKeys(card.processStepName, card.runStatus, card.assayType, wfmsflag, length);
            updatedCode['count'] = length;
            console.log(length, 'outputContainersLength', card.processStepName);
            console.log(updatedCode.labelName, 'label key');
            console.log(updatedCode.containerIcon, 'label image');
          }
      } else {
        if (inputcontainerId && inputcontainerId.inputcontainerIds) {
          const inputIds = inputcontainerId.inputcontainerIds;
          const result = inputIds.filter(ele => ele.stripId !== null);
          const length = result.length;
          updatedCode = UpdatedlabelKeys(card.processStepName, card.runStatus, card.assayType, wfmsflag, length);
          updatedCode['count'] = length;
          console.log(inputIds.length, 'inputContainersLength', card.processStepName);
          console.log(updatedCode.labelName, 'label key');
          console.log(updatedCode.containerIcon, 'label image');
        } else if (status === pending) {
          const length = 1;
          updatedCode = UpdatedlabelKeys(card.processStepName, card.runStatus, card.assayType, wfmsflag, length);
          updatedCode['count'] = 1;
          const { containerId, containertype } = runCardResponse;
          finalProcessedCard = { ...finalProcessedCard, containerId, containertype };
        }
      }

      // console.log(updatedCode, 'updatedcode');

    finalProcessedCard = { ...finalProcessedCard, ...card, colorCode, labelName, imagePath };
    finalProcessedCard = {...finalProcessedCard, updatedCode };







    if (finalProcessedCard) {
      /* Changing the process stepname inprogress to ongoing */
      finalProcessedCard.runStatus =
        card.runStatus === STATUS_CODES.IN_PROGRESS
          ? STATUS_CODES.ONGOING
          : card.runStatus;
    }

    // console.log(finalProcessedCard, 'status code');

    /* returning the finalprocessedcard response */
    return finalProcessedCard;
  }

  /**
   * it filter holdes the pending cards data.
   * @param runCardResponse run card details
   */
  public _pendingRunCardMapper(runCardResponse: any) {
    const { processStepName, outputcontainerIds: id, runStatus, assayType } = runCardResponse;
    /* Cheking the pendingprocess stepnames for combinging the records */
    const pendingProcessName = `${processStepName} Pending`.replace(
      'LP',
      this.lpProcessNames
    );

    /* If process stepname is available in rundetails checking */
    if (this.runDetails[pendingProcessName]) {
      // tslint:disable-next-line:radix
      const totalSampleCount = parseInt(
        id.map((sample: any) => sample.samplecounts)
          .reduce((a: any, b: any) => parseInt(a, 10) + parseInt(b, 10), 0)
      );
      // tslint:disable-next-line:radix
      let total = parseInt(this.runDetails[pendingProcessName][0].totalSamplecount);

      total += totalSampleCount;
      this.runDetails[pendingProcessName][0].totalSamplecount = total;

      if (id && id.length) {
        const outputIds = id;
        const length = outputIds.length;
        this.runDetails[pendingProcessName][0].updatedCode.count += length;
      }
      const countInfo = this.runDetails[pendingProcessName][0].updatedCode.count;
      const newLabel = UpdatedlabelKeys(processStepName, runStatus, assayType, '', countInfo);
      this.runDetails[pendingProcessName][0].updatedCode['labelName'] = newLabel.labelName;
      const nextProcessStepName = getUpdatedPRocessStepName(processStepName);
      this.runDetails[pendingProcessName][0]['nextProcessStepName'] = nextProcessStepName;
      console.log(this.runDetails[pendingProcessName][0], 'run details component');
      console.log(runCardResponse, 'runcard response');
    } else {
      const pendingRun = this._runCardmapper(runCardResponse);
      pendingRun.updatedCode['count'] = id.length;
      const countInfo = pendingRun.updatedCode['count'];
      const newLabel = UpdatedlabelKeys(processStepName, runStatus, assayType, '', countInfo);
      pendingRun.updatedCode['labelName'] = newLabel.labelName;
      console.log(pendingRun, 'pending cardInfo');
      delete pendingRun.deviceRunId;
      pendingRun.runStatus = 'Completed';
      pendingRun.colorCode.color = 'pending';
      pendingRun['wfmsflag'] = runCardResponse.wfmsflag;
      console.log(pendingRun, 'pending run');
      const nextProcessStepName = getUpdatedPRocessStepName(processStepName);
      pendingRun['nextProcessStepName'] = nextProcessStepName;
      this.runDetails[pendingProcessName] = [pendingRun];
    }

    // // console.log('pendingRunCardMapper', this.runDetails);
  }

  /**
   * navigate to contaner sample by passing assay type and device id
   */
  public goTocontainerSamples() {
    this._router.navigate(['/workflow/containersamples']);
  }

  onFilter(event: FilterResult) {
    // console.log('onFilter', this.completedtabList);
    this.filterOption = event.filterOptions;
    this.filterOptionCount = event.filterCount;
    // this.completedtabList = event.filteredList;
  }

  onFilterOptionCount(event) {
    this.filterOptionCount = event;
  }

}
