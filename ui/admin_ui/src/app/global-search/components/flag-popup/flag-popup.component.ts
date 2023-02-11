import { Component, OnInit, Input, ViewChild, OnChanges, Output, EventEmitter, OnDestroy } from '@angular/core';
import { MAT_MENU_SCROLL_STRATEGY, MatMenuTrigger } from '@angular/material';
import { Overlay, BlockScrollStrategy } from '@angular/cdk/overlay';
import { TranslateService } from '@ngx-translate/core';
import { SharedService } from '../../../services/shared.service';

export function scrollFactory(overlay: Overlay): () => BlockScrollStrategy {
  return () => overlay.scrollStrategies.block();
}

export type Flag = {
  flagCode?: string;
  flagDescription?: string;
  flagIcon?: string;
}

export enum DEVICE_TYPE {
  "MP24" = "mp24",
  "LP24" = "lp24",
  "MP96" = "mp96",
  "dpcr" = "dpcr"
}

@Component({
  selector: 'app-flag-popup',
  templateUrl: './flag-popup.component.html',
  styleUrls: ['./flag-popup.component.scss'],
  providers: [
    { provide: MAT_MENU_SCROLL_STRATEGY, useFactory: scrollFactory, deps: [Overlay] }
  ]
})
export class FlagPopupComponent implements OnInit, OnChanges, OnDestroy {

  @Input('sampleInput') sampleInput;
  @Output() getResponse: EventEmitter<any> = new EventEmitter();
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;

  enablepopup = false;
  flags: Flag[] = [];
  primaryFlagIcon: string;

  // Do not have story for HTP in Sprint 12 so tempHtpFlagSeverity came in picture.
  tempHtpFlagSeverity: string = 'warning';

  constructor(private _translate: TranslateService, private _sharedSvc: SharedService) { }

  ngOnInit() {
  }

  ngOnChanges() {
    //console.log(this.sampleInput, 'sampleInput****');
    //this.flags = this.getFormattedFlags("F1 vzvxcvxcv vxcvxcvxc xvxcvxcvxcv xvxcvxcvxcv xcvxcvxcvxc, F2");
    this.flags = this.getFormattedFlags(this.sampleInput.flags);
  }

  ngOnDestroy() {

  }

  isEmptyFlag (value) {
    if (value === '' || !value) {
      return true;
    } else {
      return false;
    }
  }

  public goBack() {
  }

  openMyMenu() {
    this.trigger.openMenu();
  }

  closeMyMenu() {
    this.trigger.closeMenu();
  }

  getFormattedFlags(flags: string){
    let formattedFlags: Flag[] = [];
    let flagArray = flags ? flags.split(',').map(flagCode=> flagCode ) : null;
    let browserLang = this._translate.currentLang;
    let translations = this._translate.translations[browserLang];
    let severityList = this._sharedSvc.getSharedData('flags');
    let severityOrder = ["critical", "fatal", "warning", "information"];
    if(this.sampleInput.accessioningId === 'Flow95'){
      console.log("Bikash test", flagArray);
    }
    if(flagArray){
      for(let i = 0; i < flagArray.length; i++){
        let formattedFlag: Flag = {} as Flag;
        let splittedFlag = flagArray[i].trim().split(' ');
        let deviceType = this.getDeviceType(this.sampleInput);
        let deviceTranslations = translations[deviceType];
        let deviceFlagKeys = deviceTranslations ? Object.keys(deviceTranslations) : null;
        let flagSeverity;
        
        formattedFlag.flagCode = splittedFlag[0];
        formattedFlag.flagDescription = deviceFlagKeys && deviceFlagKeys.indexOf(formattedFlag.flagCode) > -1 ? `${deviceType}.${formattedFlag.flagCode}` : null;
        flagSeverity = severityList[deviceType] ? severityList[deviceType][formattedFlag.flagCode] : null;

        if(flagSeverity){
          formattedFlag.flagIcon =  flagSeverity.toLowerCase();
          if(!this.primaryFlagIcon){
            this.primaryFlagIcon = formattedFlag.flagIcon
          }else if(this.primaryFlagIcon && severityOrder.indexOf(this.primaryFlagIcon) > severityOrder.indexOf(formattedFlag.flagIcon) ){
            this.primaryFlagIcon = formattedFlag.flagIcon;
          }
        }else{
          formattedFlag.flagIcon = this.tempHtpFlagSeverity;
          this.primaryFlagIcon = this.primaryFlagIcon ? this.primaryFlagIcon : this.tempHtpFlagSeverity;
        }
        //console.log('Trans', this.primaryFlagIcon, flags);
        formattedFlags.push(formattedFlag);
      }
    }
    //console.log("formattedFlags", formattedFlags);
    return formattedFlags;
  }

  getDeviceType(sample){
    let deviceType;
    let flowType = sample.processStepName ? sample.processStepName : sample.workflowType;
    let assayWorkflowType = `${sample.assayType.toLowerCase()}-${flowType.trim().split(" ").join("-").split("/").join("-").toLowerCase()}`;
    deviceType = DEVICE_FLAGS.filter(deviceFlag => deviceFlag.assayWorkflowType === assayWorkflowType)[0].deviceType;

    return deviceType;
  }
}

export const DEVICE_FLAGS = [{
  assayWorkflowType: "nipthtp-lp-post-pcr-pooling",
  deviceType: "lp24"
},{
  assayWorkflowType: "nipthtp-lp-pre-pcr-pooling",
  deviceType: "lp24"
},{
  assayWorkflowType: "nipthtp-lp-sequencing-prep",
  deviceType: "lp24"
},{
  assayWorkflowType: "nipthtp-lp-pre-pcr",
  deviceType: "lp24"
},{
  assayWorkflowType: "niptdpcr-library-preparation",
  deviceType: "lp24"
},{
  assayWorkflowType: "nipthtp-na-extraction",
  deviceType: "lp24"
},{
  assayWorkflowType: "niptdpcr-na-extraction",
  deviceType: "mp96"
},{
  assayWorkflowType: "nipthtp-dpcr",
  deviceType: "dpcr"
},{
  assayWorkflowType: "niptdpcr-dpcr",
  deviceType: "dpcr"
}];