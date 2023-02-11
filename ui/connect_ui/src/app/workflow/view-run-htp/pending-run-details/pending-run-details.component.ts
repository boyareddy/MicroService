import { Component, OnInit, OnDestroy } from '@angular/core';
import { HeaderInfo } from 'src/app/shared/header.model';
import { TranslateService } from '@ngx-translate/core';
import { WorkflowService } from '../../workflow.service';
import { ActivatedRoute } from '@angular/router';
import { getRunDetailsLableInfo } from '../../workflow-dashboard.util';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';

@Component({
  selector: 'app-pending-run-details',
  templateUrl: './pending-run-details.component.html',
  styleUrls: ['./pending-run-details.component.scss']
})
export class PendingRunDetailsComponent implements OnInit, OnDestroy {

  headerInfo: HeaderInfo = {
    headerName: 'Workflow',
    isBackRequired: true,
    curPage: ''
  };

  public pendingRunList = [];
  public pendingRunItem;
  public processstepname;
  public assayType;
  public workFlowSteps;
  public runDetailsRes;
  public runPedingList = [];
  public displayedColumns;
  public columnsToDisplay;
  public isSprinerOn = false;
  public rectPro;
  public translations;
  public displayImg: string;
  public stripLabel: string;

  constructor(
    private _workflowService: WorkflowService,
    private _acRoute: ActivatedRoute,
    private translate: TranslateService,
    private __snackBarSvc: SnackBarService
  ) { }

  ngOnInit() {
    this._acRoute.queryParams.subscribe(params => {
      this.processstepname = params['processstepname'];
      this.assayType = params['assayType'];
      const { labelName, labelImage } = getRunDetailsLableInfo(this.processstepname, 'Completed', this.assayType);
      this.stripLabel = labelName;
      this.displayImg = labelImage;
      this.setTableHeader();
      this.getRunInformation();
      // Transalation for header content
      this.translations = this.translate.translations[this.translate.currentLang];
      const proStep = this.translations[this.processstepname] ? this.translations[this.processstepname] : this.processstepname;
      const lftSmp = this.translations['LEFT_OVER_SAMPLES'] ? this.translations['LEFT_OVER_SAMPLES'] : 'Leftover samples';
      this.headerInfo.curPage = `${proStep}: ${lftSmp}`;
    });
  }

  ngOnDestroy() {
    this.isSprinerOn = false;
  }

  setTableHeader() {
    if (this.processstepname.toLowerCase().indexOf('prep') !== -1) {
      this.displayedColumns = ['accessioningId', 'assaytype', 'status', 'flags', 'comments'];
      this.columnsToDisplay = ['Accessioning ID', 'Assay', 'Status', 'Flags', 'Comments'];
    } else {
        this.displayedColumns = ['accessioningId', 'assaytype', 'position', 'status', 'flags', 'comments'];
        this.columnsToDisplay = ['Accessioning ID', 'Assay', 'Position', 'Status', 'Flags', 'Comments'];
    }
  }

  getRunInformation() {
     const queryParam1 = {
      'assayTypeDetail': this.assayType,
      'processStepVal': this.processstepname
    };
    this.isSprinerOn = true;
    this._workflowService.getRunInfoInCompletedByWFS(queryParam1).subscribe( (res) => {
        this.isSprinerOn = false;
        this.runDetailsRes = res;
        this.createJsonForWorkFlow();
    },
    error => {
      this.isSprinerOn = false;
      console.log('Error on getting Run Information in completed status', error);
    });
  }

  createJsonForWorkFlow() {
    if (this.runDetailsRes) {
      this.runPedingList = this.runDetailsRes.filter((value) => value.wfmsflag.toLowerCase() === 'pending');
      this.pendingRunList = [];
      if (this.runPedingList !== null && this.runPedingList !== undefined && this.runPedingList.length > 0) {
        this.runPedingList.forEach((value, index) => {
            const r_details = (({
              deviceRunId, runStatus: status, processStepName, runResultId, assayType,
              runStartTime, runCompletionTime, operatorName, runFlag
            }) => ({
              deviceRunId, status, processStepName, runResultId, assayType, runStartTime, runCompletionTime, operatorName, runFlag
            }))(value);
            this.pendingRunItem = {...r_details};
            this.pendingRunItem = {...this.pendingRunItem, totalSampleCount: value.sampleResults.length};
            const runsampledetails = [];
            value.sampleResults.forEach((value_, index_) => {
              let isAlreadyAvail = false;
              let indexes = 0;
              let _SL = 0;
              if (runsampledetails.length > 0) {
                const isStipIdInd = runsampledetails.findIndex(item => item.stripId === value_.outputContainerId);
                if (isStipIdInd != null) {
                    indexes = isStipIdInd;
                    isAlreadyAvail = true;
                    _SL = runsampledetails[isStipIdInd]['sampledetails'].length;
                } else {
                    indexes = runsampledetails.length;
                }
              }
              if (!isAlreadyAvail) {
                runsampledetails[indexes] = {};
                runsampledetails[indexes]['sampledetails'] = [];
                runsampledetails[indexes]['stripId'] = value_.outputContainerId;
                runsampledetails[indexes]['runResultId'] = value_.runResultId;
              }
              const _SM = (({
                accesssioningId: accessioningId, comments, outputContainerType, flag: flags,
                outputContainerPosition: position, status
              }) => ({
                accessioningId, comments, outputContainerType, flags, position, status
              }) )(value_);
              // tslint:disable-next-line:max-line-length
              runsampledetails[indexes]['sampledetails'][_SL] = {..._SM, processStepName: this.processstepname, assaytype: this.assayType};
            });
            this.pendingRunItem['runsampledetails'] = runsampledetails;
            this.pendingRunList.push(this.pendingRunItem);
        });
      }
    }
  }

  /**
     * update the comments
     * @param event comments information
     */
  public updatedComments(event: any) {
    if (typeof event === 'object') {
      this._workflowService.postComments(event).subscribe(res => {
        this.getRunInformation();
      }, error => {
        console.error('error occured', error);
        this.__snackBarSvc.showErrorSnackBar('Error Occur updating comments', SnackbarClasses.errorBottom1);
      });
    }
  }
}
