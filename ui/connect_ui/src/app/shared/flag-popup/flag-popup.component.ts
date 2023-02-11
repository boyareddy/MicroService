import { Component, OnInit, Input, ViewChild, OnChanges, Output, EventEmitter, OnDestroy } from '@angular/core';
import { MAT_MENU_SCROLL_STRATEGY, MatMenuTrigger } from '@angular/material';
import { Overlay, BlockScrollStrategy } from '@angular/cdk/overlay';
import { TranslateService } from '@ngx-translate/core';
import { SharedService } from '../shared.service';
import { getDeviceType } from './flags.util';
export function scrollFactory(overlay: Overlay): () => BlockScrollStrategy {
  return () => overlay.scrollStrategies.block();
}

// tslint:disable-next-line:interface-over-type-literal
export type Flag = {
  flagCode?: string;
  flagDescription?: string;
  flagIcon?: string;
};


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

  public enablepopup = false;
  public flags: Flag[] = [];
  public primaryFlagIcon: string;

  // default flag severity of the flag.
  tempHtpFlagSeverity = 'warning';

  constructor(private _translate: TranslateService, private _sharedSvc: SharedService) { }

  ngOnInit() {
  }

  ngOnChanges() {
    // console.log(this.sampleInput, 'sampleInput****');
    // this.flags = this.getFormattedFlags('F1 vzvxcvxcv vxcvxcvxc xvxcvxcvxcv xvxcvxcvxcv xcvxcvxcvxc, F2');
    this.flags = this.getFormattedFlags(this.sampleInput.flags);
  }

  ngOnDestroy() {

  }

  isEmptyFlag(value) {
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

  /**
   * assign the flag code along flag description
   * @param flags flagsInformation
   */
  getFormattedFlags(flags: string) {
    const formattedFlags: Flag[] = [];
    const flagArray = flags ? flags.split(',').map(flagCode => flagCode) : null;
    /* Localization for the flags */
    const browserLang = this._translate.currentLang;
    const translations = this._translate.translations[browserLang];

    /* get data from the shared service using "flags" key */
    const severityList = this._sharedSvc.getData('flags');

    /* default severity order of flags */
    const severityOrder = ['critical', 'fatal', 'warning', 'information'];

    // if (this.sampleInput.accessioningId === 'Flow95') {
    //   console.log('Bikash test', flagArray);
    // }

    /* Validating the flags array if flags available it process*/
    if (flagArray && flagArray.length > 0) {
      for (let i = 0; i < flagArray.length; i++) {
        const formattedFlag: Flag = {} as Flag;
        const splittedFlag = flagArray[i].trim().split(' ');

        /* fetching the device type by passing the sample input */
        const deviceType = getDeviceType(this.sampleInput);

        /* By passing device type fetching the translation details */
        const deviceTranslations = translations[deviceType];

        /* fetching the keys flags data */
        const deviceFlagKeys = deviceTranslations ? Object.keys(deviceTranslations) : null;

        let flagSeverity;
        formattedFlag.flagCode = splittedFlag[0];

        formattedFlag.flagDescription = deviceFlagKeys && deviceFlagKeys.indexOf(formattedFlag.flagCode) > -1
          ? `${deviceType}.${formattedFlag.flagCode}`
          : null;

        /* get the flag severity by passing argument as deviceType */
        flagSeverity = severityList[deviceType] ? severityList[deviceType][formattedFlag.flagCode] : null;

        /* checking the flag icon severity */
        if (flagSeverity) {
          formattedFlag.flagIcon = flagSeverity.toLowerCase();
          if (!this.primaryFlagIcon) {
            this.primaryFlagIcon = formattedFlag.flagIcon;
          } else if (this.primaryFlagIcon && severityOrder.indexOf(this.primaryFlagIcon) > severityOrder.indexOf(formattedFlag.flagIcon)) {
            this.primaryFlagIcon = formattedFlag.flagIcon;
          }
        } else {
          formattedFlag.flagIcon = this.tempHtpFlagSeverity;
          this.primaryFlagIcon = this.primaryFlagIcon ? this.primaryFlagIcon : this.tempHtpFlagSeverity;
        }
        // console.log('Trans', this.primaryFlagIcon, flags);
        formattedFlags.push(formattedFlag);
      }
    }
    // console.log('formattedFlags', formattedFlags);
    return formattedFlags;
  }

}
