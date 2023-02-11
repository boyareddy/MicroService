import { Component, OnInit, Input, OnChanges, EventEmitter, Output } from '@angular/core';
import { WorkflowService } from '../../../workflow.service';
import * as moment from 'moment-timezone';
import { SharedService } from '../../../../shared/shared.service';
import { ActivatedRoute } from '@angular/router';
import { processStepNames, LowerCaseStatus, BasicAssayType, ConstantNames } from 'src/app/workflow/workflow-dashboard.util';
import { MatIconInfoService } from 'src/app/shared/mat-icons/mat-icon-info.service';

@Component({
  selector: 'app-sample-run-card',
  templateUrl: './sample-run-card.component.html',
  styleUrls: ['./sample-run-card.component.scss']
})
export class SampleRunCardComponent implements OnInit, OnChanges {

  @Input() sampleruncard;
  @Output() commentsResponse: EventEmitter<any> = new EventEmitter();
  public runsample: any;
  public samplevol: any;
  public deviceType: any;
  public deviceId: any;
  public reagentslist: any;
  objectKeys = Object.keys;
  public inputReagentCardKey = 'reagent';
  public inputSampleCardKey = 'sample';
  newReagentsList = [];

  constructor(private _workflowService: WorkflowService,
    private _sharedService: SharedService,
    private _acRoute: ActivatedRoute,
    private _iconService: MatIconInfoService) { }

  ngOnInit() {
    this._iconService.initIcons();
  }
  ngOnChanges() {
    console.log(this.sampleruncard, 'samp');
    if (this.sampleruncard) {
      if (this.sampleruncard.processStepName !== processStepNames.Sequencing) {
        this.getDeviceType(this.sampleruncard.assayType, this.sampleruncard.processStepName);
      }
      if (this.sampleruncard.processStepName === processStepNames.Sequencing &&
            (this.sampleruncard.status.toLowerCase().indexOf(LowerCaseStatus.completed) > -1 ||
              this.sampleruncard.status.toLowerCase().indexOf(LowerCaseStatus.failed) > -1)) {
        this.creaJsonForReagents();
      }
      if (this.sampleruncard.assayType === BasicAssayType.NIPTDPCR &&
        this.sampleruncard.processStepName === processStepNames.NA_Extraction &&
        (this.sampleruncard.status.toLowerCase().indexOf(LowerCaseStatus.completed) > -1 ||
          this.sampleruncard.status.toLowerCase().indexOf(LowerCaseStatus.failed) > -1)) {
            this.createReagentsForNA_Extraction();
          }
    }
  }

  getDeviceType(assayType, processStepName) {
    console.log(processStepName, 'sdfjkajsdhlasukdja');
    this._workflowService.getWorkFlowSteps(assayType).subscribe(resp => {
      this.deviceType = resp;
      this.deviceType.filter(ele => {
        if (ele.processStepName === processStepName) {
          this.deviceId = ele.deviceType;
        }
      });
      if (this.deviceId) {
        const queryParam = {
          'assayTypeDetail': this.sampleruncard.assayType,
          'deviceTypeID':  this.deviceId
      };
        this.getVolumeList(queryParam);
      }
    });
  }

  getVolumeList(queryParam) {
    this._workflowService.getSampleVolumes(queryParam).subscribe(resp => {
    this.samplevol = resp[0];
    console.log(this.samplevol, 'samp');
    });
}


  navigate(deviceId) {
    // tslint:disable-next-line:max-line-length
    window.location.href = `${this._sharedService.getData('appProperties').protocol}://${this._sharedService.getData('appProperties').host}:${this._sharedService.getData('appProperties').appPort}/admin_ui/#/device-detail/${deviceId}`;
  }


  creaJsonForReagents() {
    const list = [];
    this.reagentslist = [];
    const reagentKeys = ['seqkit', 'system'];
    reagentKeys.forEach(reagentName => {
      const newJSON =
      this.sampleruncard.uniqueReagentsAndConsumables.filter(ele => ele.attributeName.toLowerCase().indexOf(reagentName) > -1);
      if (newJSON) {
        const reagentsInfo = {};
        reagentsInfo['attributeName'] = reagentName;
        newJSON.forEach(element => {
          const { attributeValue, attributeName } = element;
          if (attributeName.toLowerCase().indexOf('expiration') > -1) {
            reagentsInfo['expiration'] = attributeValue;
          } else if (attributeName.toLowerCase().indexOf('partnumber') > -1) {
            reagentsInfo['partNumber'] = attributeValue;
          }
        });
        this.reagentslist.push(reagentsInfo);
      }
      console.log(list, 'new JSON data');
    });
  }

  public createReagentsForNA_Extraction() {
    this.newReagentsList = [];
    const reagentsInfo = [];
    const { uniqueReagentsAndConsumables: reagents } = this.sampleruncard;
    reagents.forEach(ele => {
      reagentsInfo[ele.attributeName]
        ? reagentsInfo[ele.attributeName].push(ele.attributeValue)
        : reagentsInfo[ele.attributeName] = [ele.attributeValue];
    });
    console.log(typeof reagents);
    if (reagentsInfo) {
      for (const key in reagentsInfo) {
        if (reagentsInfo.hasOwnProperty(key)) {
          let labelName;
          if (key === ConstantNames.Internal_Control) {
            labelName = 'internalControl';
          } else if (key === ConstantNames.Reagents) {
            labelName = 'reagents';
          }
          const newReagent = {
            attributeName: labelName,
            attributeValue: reagentsInfo[key]
          };
          this.newReagentsList.push(newReagent);
        }
      }
      this.newReagentsList.sort((a, b) => {
        if (a.attributeName < b.attributeName) {
          return -1;
        } else if ((a.attributeName > b.attributeName)) {
          return 1;
        } else {
          return 0;
        }
      });
    }
    console.log(this.newReagentsList, 'new reagets list');
  }

  getCompleteionTime() {
    if (this.sampleruncard) {
      const runDetail = this.sampleruncard.processStepName.toLowerCase();
      const runStatus = this.sampleruncard.status.toLowerCase();
      return  (runDetail.indexOf('pre') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 )) ||
              (runDetail.indexOf('extraction') !== -1 &&
                  (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1  || runStatus.indexOf('failed') !== -1)) ||
              (runDetail.indexOf('post') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 )) ||
              (runDetail.indexOf('sequencing') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 ||
                runStatus.indexOf('failed') !== -1 )) ||
              (runDetail.indexOf('preparation') !== -1 && runStatus.indexOf('failed') !== -1 ) ||
              (runDetail.indexOf('dpcr') !== -1 && runStatus.indexOf('completed') !== -1 ) ||
              (runDetail.indexOf('dpcr') !== -1 && runStatus.indexOf('aborted') !== -1 );
    }
  }

  getoutputContainerType() {
    if (this.sampleruncard) {
      const runDetail = this.sampleruncard.processStepName.toLowerCase();
      const runStatus = this.sampleruncard.status.toLowerCase();

      return (runDetail.indexOf('pre') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 )) ||
            (runDetail.indexOf('extraction') !== -1 &&
                (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1  || runStatus.indexOf('failed') !== -1)) ||
            (runDetail.indexOf('extraction') !== -1 && runStatus.indexOf() !== 'ongoing') ||
            (runDetail.indexOf('post') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 )) ||
            (runDetail.indexOf('preparation') !== -1 && runStatus.indexOf('failed') !== -1 );
    }
  }

  getRunFlags() {
    if (this.sampleruncard) {
      const runDetail = this.sampleruncard.processStepName.toLowerCase();
      const runStatus = this.sampleruncard.status.toLowerCase();

      return  (runDetail.indexOf('pre') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 )) ||
              (runDetail.indexOf('extraction') !== -1 &&
                  (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1  || runStatus.indexOf('failed') !== -1)) ||
              (runDetail.indexOf('post') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 )) ||
              (runDetail.indexOf('sequencing') !== -1 && (runStatus.indexOf('completed') !== -1 || runStatus.indexOf('aborted') !== -1 ||
              runStatus.indexOf('failed') !== -1)) ||
              (runDetail.indexOf('preparation') !== -1 && runStatus.indexOf('failed') !== -1 );
    }
  }

  response(event: any) {
    console.log(event);
    this.commentsResponse.emit(event);
  }


  update(el: Element, comment: string) {
    if (comment == null) { return; }
    // copy and mutate
    const event = {
      comments: comment,
      runResultId: this.sampleruncard.runResultId
    };
    console.log(event);
    this.commentsResponse.emit(event);
  }
}
