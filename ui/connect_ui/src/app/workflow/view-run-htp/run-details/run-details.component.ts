import { Component, OnInit, OnDestroy, AfterViewInit, ElementRef } from '@angular/core';
import { WorkflowService } from '../../workflow.service';
import { HeaderInfo } from '../../../shared/header.model';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { getHoursMin } from 'src/app/shared/functions/HoursSeconds';
import { LowerCaseStatus, getProcessStatus, getRunDetailsLableInfo, LablesImagePath, getCss,
    imgPath, processStepNames, BasicAssayType, ConstantNames } from '../../workflow-dashboard.util';
import { STATUS_CODES } from '../../model';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedService } from 'src/app/shared/shared.service';
import { WarningRequiredService } from 'src/app/shared/inline-htmls/warning-required.service';

@Component({
    selector: 'app-run-details',
    templateUrl: './run-details.component.html',
    styleUrls: ['./run-details.component.scss']
})
export class RunDetailsComponent implements OnInit, OnDestroy, AfterViewInit {

    public runDetails: any;
    public completedrun: any;
    public runId: any;
    public displayedColumns: any;
    public columnsToDisplay: any;
    public runSubscribe: any;
    public processStepName: any;
    public isSprinerOn = false;
    public rectPro: any;

    headerInfo: HeaderInfo = {
        headerName: 'Workflow',
        isBackRequired: true,
        curPage: '',
    };

    constructor(private _workflowService: WorkflowService,
        private translate: TranslateService,
        private _acRoute: ActivatedRoute,
        private _snackBarSvc: SnackBarService,
        private elem: ElementRef,
        private _sharedService: SharedService,
        private _router: Router,
        private _warningService: WarningRequiredService
    ) { }

    ngOnInit() {
        const history = window.history;
        if (history.length === 1) {
            this.headerInfo.navigateUrl = 'workflow';
        }
        this._warningService.getLastProcessStepName();
        this._acRoute.params.subscribe(params => {
            if (this.runSubscribe !== undefined) {
                this.runSubscribe.unsubscribe();
            }
            // this.runDetails = [];
            this.runId = params['runId'];
            if (this.runId !== null || this.runId !== undefined || this.runId !== '') {
                this.getCompletedrun(this.runId);
            }
        });
    }

    ngOnDestroy() {
        if (this.runSubscribe !== undefined) {
            this.runSubscribe.unsubscribe();
        }
        this.isSprinerOn = false;
    }

    ngAfterViewInit() {
        setTimeout(() => {
            if (this.isSprinerOn) {
                const rect = this.elem.nativeElement.querySelectorAll('.run-details-page')[0].getBoundingClientRect();
                this.rectPro = rect;
                this.isSprinerOn = false;
            }
        }, 600);
    }

    /**
     * fetch the rundetails
     * @param runId runID information
     */
    public getCompletedrun(runId) {
        this.isSprinerOn = true;
        this.runSubscribe = this._workflowService.getHtpRunList(runId).subscribe(resp => {
            this.isSprinerOn = false;
            console.log(this.isSprinerOn, 'this.isSprinerOn');
            const runArray = [];
            runArray[0] = resp;
            if (runArray && runArray.length > 0) {
                this.fetchRunDetails(runArray);
            }
            this.completedrun = runArray;
            const protocol = this.translate.get(this.completedrun[0].processStepName, this.completedrun[0].processStepName);
            let protocolName;
            protocol.subscribe((res) => {
                protocolName = res;
            });
            if (this.completedrun[0].deviceRunId !== null &&
                this.completedrun[0].deviceRunId !== undefined &&
                this.completedrun[0].deviceRunId !== '') {
                this.headerInfo.curPage = `${protocolName}: ${this.completedrun[0].deviceRunId}`;
                this.processStepName = this.completedrun[0].processStepName;
            } else {
                this.headerInfo.curPage = `${protocolName}: -`;
            }
        }, (error: HttpErrorResponse) => {
            const translations = this.translate.translations[this.translate.currentLang];
            this.isSprinerOn = false;
            // console.error(error, 'error occured');
            if (error.status === 400) {
                const message = `${translations.runWorkflow.validations.errorNoRun}: ${this.runId}`;
                console.log(message);
                this._sharedService.setData('runDetailsError', message);
                this._router.navigate(['/workflow']);
            } else {
                const message = `${translations.runWorkflow.validations.connectivityIssue}`;
                console.log(message);
                this._sharedService.setData('runDetailsError', message);
                this._router.navigate(['/workflow']);
            }
        });
    }

    /**
     * filter the response
     * @param response run details Info
     */
    public fetchRunDetails(response: any) {
        let totalRunDetails: any;
        let isOngoingProgressbarRequired = false;
        let runRemainingTime = '';
        const mainResponse = (({ assayType, comments, deviceId, deviceRunId,
            operatorName, processStepName, runCompletionTime,
            runFlag, runResultId, runStartTime,
            uniqueReagentsAndConsumables }) =>
            ({
                assayType, comments, deviceId, deviceRunId,
                operatorName, processStepName, runCompletionTime, runFlag,
                runResultId, runStartTime,
                uniqueReagentsAndConsumables
            }))(response[0]);

        const { runStatus, runRemainingTime: remainingTime } = response[0];
        const status = getProcessStatus(runStatus);
        const color = getCss(status);
        const imagePath = imgPath(mainResponse.processStepName, runStatus, mainResponse.assayType);
        console.log(imagePath, 'image path');
        console.log(color, 'color is');
        if (status === STATUS_CODES.ONGOING) {
            isOngoingProgressbarRequired = true;
            runRemainingTime = getHoursMin(remainingTime);
        }

        const sampleResults = response[0].sampleResults;
        const totalSampleCount = sampleResults.length;
        const { displayedColumns, columnsToDisplay } = getRunDetailsLableInfo(mainResponse.processStepName, status, mainResponse.assayType);
        this.displayedColumns = displayedColumns;
        this.columnsToDisplay = columnsToDisplay;

        const outputContainerType = Array.from(new Set(sampleResults.map(sample => sample.outputContainerType)
            .filter(ele => ele !== null)))[0];
        let protocolNames = [];
        if (sampleResults && sampleResults.length > 0) {
            const ele = sampleResults[0].sampleProtocol.map(ele1 => ele1.protocolName).filter(ele2 => (ele2 !== null));
            if (ele.length > 0) {
                protocolNames =  Array.from(new Set(ele));
            }
        }
        const protocolName = protocolNames.length > 0 ? protocolNames : null;
        const newFormat = [];
        let output_folder;

        if (runStatus.toLowerCase().indexOf(LowerCaseStatus.completed) > -1 &&
                (mainResponse.processStepName === processStepNames.Sequencing ||
                mainResponse.processStepName === processStepNames.DPCR)) {
            const { runResultsDetail } = response[0];
            let outputfileLocation;
            if (runResultsDetail && runResultsDetail.length > 0) {
                if (mainResponse.assayType === BasicAssayType.NIPTHTP) {
                    outputfileLocation = runResultsDetail.find(ele => ele.attributeName.toLowerCase() === 'output_folder');
                } else if (mainResponse.assayType === BasicAssayType.NIPTDPCR) {
                    outputfileLocation = runResultsDetail.find(ele => ele.attributeName.toLowerCase() === 'dpcr analyzer filepath');
                }

                if (outputfileLocation) {
                    output_folder = outputfileLocation.attributeValue;
                } else {
                    output_folder = null;
                }
            }
        }

        sampleResults.forEach((sample: any) => {
            const filteredData = this.filterSample(sample, mainResponse.processStepName);
            const { status: sampleLevelStatus } = sample;
            const sampleStatus = sampleLevelStatus.toLowerCase();
            if (sampleStatus.indexOf(LowerCaseStatus.completed) > -1 ||
                sampleStatus.indexOf(LowerCaseStatus.failed) > -1 ||
                sampleStatus.indexOf(LowerCaseStatus.aborted) > -1 ||
                sampleStatus.indexOf(LowerCaseStatus.passed) > -1) {
                newFormat[filteredData.outputContainerId]
                    ? newFormat[filteredData.outputContainerId].push(filteredData)
                    : newFormat[filteredData.outputContainerId] = [filteredData];
            } else {
                newFormat[filteredData.inputContainerId]
                    ? newFormat[filteredData.inputContainerId].push(filteredData)
                    : newFormat[filteredData.inputContainerId] = [filteredData];
            }
        });
        const runsampledetails = this.generateId(newFormat, mainResponse.processStepName, mainResponse.assayType, status,
            mainResponse.uniqueReagentsAndConsumables);
        totalRunDetails = {
            ...mainResponse, totalSampleCount, outputContainerType, protocolName, runsampledetails,
            status, isOngoingProgressbarRequired, runRemainingTime, color, imagePath, output_folder
        };
        if (mainResponse.processStepName === processStepNames.Sequencing &&
            (status.toLowerCase().indexOf(LowerCaseStatus.completed) > -1 ||
            status.toLowerCase().indexOf(LowerCaseStatus.failed) > -1)) {
            let laneNo, consumableDevicePartNumber;
            const { runResultsDetail, uniqueReagentsAndConsumables } = response[0];
            if (runResultsDetail && runResultsDetail.length > 0) {
               const laneNoDetails = runResultsDetail.find(ele => ele.attributeName.toLowerCase() === 'laneno');
                if (laneNoDetails) {
                    laneNo = laneNoDetails.attributeValue;
                } else {
                    laneNo = null;
                }
            }
            if (uniqueReagentsAndConsumables && uniqueReagentsAndConsumables.length > 0) {
                const stripDetails =
                    uniqueReagentsAndConsumables.find(ele => ele.attributeName === ConstantNames.Consumable_Device_Part_Number);
                if (stripDetails) {
                    consumableDevicePartNumber = stripDetails.attributeValue;
                } else {
                    consumableDevicePartNumber = null;
                }
            }
            totalRunDetails = {...totalRunDetails, laneNo, consumableDevicePartNumber};
        }
        this.runDetails = totalRunDetails;
        console.log(this.runDetails);
    }

    /**
     * To create the strip id and samples
     * @param newFormat changeFomat
     * @param stepName processStepName
     * @param assayType assaytype
     * @param status run status
     */
    public generateId(newFormat: any, stepName: string, assayType: string, status: string, uniqueReagentsAndConsumables: any) {
        const GenratedId = [];
        console.log(newFormat, 'sample details');
        for (const key in newFormat) {
            if (newFormat.hasOwnProperty(key)) {
                const newId = {};
                if (stepName === processStepNames.Sequencing &&
                    (status.toLowerCase().indexOf(LowerCaseStatus.completed) > -1 ||
                    status.toLowerCase().indexOf(LowerCaseStatus.failed) > -1)) {
                        if (uniqueReagentsAndConsumables && uniqueReagentsAndConsumables.length > 0) {
                            const stripDetails =
                            uniqueReagentsAndConsumables.find(ele => ele.attributeName === ConstantNames.Consumable_Device_Part_Number);
                            if (stripDetails) {
                                newId['stripId'] = stripDetails.attributeValue;
                            } else {
                                newId['stripId'] = null;
                            }
                        } else {
                            newId['stripId'] = null;
                        }
                } else {
                    newId['stripId'] = key === 'null' ? null : key;
                }
                newId['sampledetails'] = newFormat[key];
                if (key) {
                    const { labelName, labelImage } = getRunDetailsLableInfo(stepName, status, assayType);
                    newId['labelName'] = labelName;
                    newId['labelImage'] = labelImage;
                } else {
                    newId['labelName'] = 'abSample';
                    newId['labelImage'] = LablesImagePath.ABORTED;
                }
                GenratedId.push(newId);
            }
        }
        return GenratedId;
    }

    /**
     * filter the unused samples information
     * @param runDetails samples details
     * @param processStepName processstepname
     */
    public filterSample(runDetails: any, processStepName: string) {
        let status: string;
        let position: string;
        const newRunDetails =
            (({ accesssioningId: accessioningId,
                assayType: assaytype,
                comments, flag: flags,
                outputContainerId,
                inputContainerId,
                mandatoryFieldMissing
                }) =>
                ({
                    accessioningId,
                    assaytype,
                    comments,
                    flags,
                    outputContainerId,
                    inputContainerId,
                    mandatoryFieldMissing
                }))(runDetails);
        const { status: sampleStatus } = runDetails;
        if (sampleStatus.toLowerCase().indexOf('inprogress') !== -1 ||
            sampleStatus.toLowerCase().indexOf('inprocess') !== -1 ||
            sampleStatus.toLowerCase().indexOf('started') !== -1) {
                status = 'Ongoing';
        } else {
            status = sampleStatus;
        }
        if (status.toLowerCase().indexOf(LowerCaseStatus.completed) > -1 ||
            status.toLowerCase().indexOf(LowerCaseStatus.failed) > -1 ||
                status.toLowerCase().indexOf(LowerCaseStatus.aborted) > -1) {
            const { outputContainerPosition } = runDetails;
            position = outputContainerPosition;
        } else {
            const { inputContainerPosition } = runDetails;
            position = inputContainerPosition;
        }
        newRunDetails['position'] = position;
        newRunDetails['status'] = status;
        newRunDetails['processStepName'] = processStepName;
        return newRunDetails;
    }

    /**
     * update the comments
     * @param event comments information
     */
    public updatedComments(event: any) {
        if (typeof event === 'object') {
            this._workflowService.postComments(event).subscribe(res => {
                if (this.runSubscribe !== undefined) {
                    this.runSubscribe.unsubscribe();
                }
                this.getCompletedrun(this.runId);
            }, error => {
                console.error('error occured', error);
                this._snackBarSvc.showErrorSnackBar('Error Occured updating comments', 'failed-snackbar-bottom1');
            });
        }
    }
}
