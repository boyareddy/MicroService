import { Injectable } from '@angular/core';
import { Sample } from './flag.model';
import { SharedService } from '../shared.service';
import { Flag } from './flag-popup.component';
import { getDeviceType } from './flags.util';

@Injectable({
  providedIn: 'root'
})
export class FlagService {

  // Do not have story for HTP in Sprint 12 so tempHtpFlagSeverity came in picture.
  tempHtpFlagSeverity: string = 'warning';

  constructor(private _sharedSvc: SharedService) { }

  getSeverity(sample: Sample): string{
    let primaryFlagIcon: string;
    let formattedFlags: Flag[] = [];
    let flagArray = sample.flag ? sample.flag.split(',').map( flagCode => flagCode ) : null;
    let severityList = this._sharedSvc.getData('flags');
    let severityOrder = ["critical", "fatal", "warning", "information"];
    if(flagArray && flagArray.join("").trim() !== ""){
      //primaryFlagIcon = this.tempHtpFlagSeverity;
      for(let i = 0; i < flagArray.length; i++){
        let formattedFlag: Flag = {} as Flag;
        let splittedFlag = flagArray[i].trim().split(' ');
        let deviceType = getDeviceType(sample);
        let flagSeverity;
        
        formattedFlag.flagCode = splittedFlag[0];
        flagSeverity = severityList[deviceType] ? severityList[deviceType][formattedFlag.flagCode] : null;

        if(flagSeverity){
          formattedFlag.flagIcon =  flagSeverity.toLowerCase();
          if(!primaryFlagIcon){
            primaryFlagIcon = formattedFlag.flagIcon
          }else if(primaryFlagIcon && severityOrder.indexOf(primaryFlagIcon) > severityOrder.indexOf(formattedFlag.flagIcon) ){
            primaryFlagIcon = formattedFlag.flagIcon;
          }
        }else{
          formattedFlag.flagIcon = this.tempHtpFlagSeverity;
          primaryFlagIcon = primaryFlagIcon ? primaryFlagIcon : this.tempHtpFlagSeverity;
        }
      }
    }
    return primaryFlagIcon;
  }
}
