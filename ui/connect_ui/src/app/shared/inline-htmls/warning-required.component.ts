import { Component, OnChanges, Input, HostListener, OnInit } from '@angular/core';
import { ProcessStepNames, IconsInfo } from '../../standard-names/constants';
import { MatIconInfoService } from '../mat-icons/mat-icon-info.service';
import { TranslateService } from '@ngx-translate/core';
import { BasicAssayType } from 'src/app/workflow/workflow-dashboard.util';
import { SharedService } from '../shared.service';

@Component({
    selector: 'app-warning-required',
    template: `
        <ng-container *ngIf='matIconInfo'>
        <mat-icon *ngIf='isMandatory; else notMandatory' [svgIcon]="matIconInfo" class="rc-icon1"
        [tooltip]="missingPHI" [placement]='position.placement'
        hide-delay='0' animation-duration='0' tooltip-class='ng-tooltip-custom1'>
        </mat-icon>
        <ng-template #notMandatory>
        <mat-icon [svgIcon]="matIconInfo" class="rc-icon1"></mat-icon>
        </ng-template>
        </ng-container>
    `,
    styles: []
})

export class AppWarningRequiredComponent implements OnChanges, OnInit {
    @Input('processStepName') processStepName: string;
    @Input('isMandatory') isMandatory: boolean;
    @Input('fieldName') fieldName: string;
    @Input('fieldValue') fieldValue: any;
    @Input('requiredMandatoryInfo') requiredMandatoryInfo = [];
    @Input('assayType') assayType: string;

    public stepName = ProcessStepNames.ANALYSIS;
    public matIconInfo;
    public position = {
        'placement': 'bottom'
    };
    public missingPHI;

    @HostListener('mousemove', ['$event']) onMousemove(event: MouseEvent) {
        if (event.clientY > 600) {
          this.position.placement = 'top';
        } else {
          this.position.placement = 'bottom';
        }
    }
    // public requiredMandatoryInfo = [
    //     {
    //         'fieldName': 'Maternal age',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Gestational age',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'IVF status',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Egg Donor',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Egg donor age',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Number of fetus',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'First name',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Last name',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Medical record number',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'DOB',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Referring clinician',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     },
    //     {
    //         'fieldName': 'Laboratory ID',
    //         'minVal': null,
    //         'maxVal': null,
    //         'expression': 'NA',
    //         'assayType': 'NIPTHTP',
    //         'isMandatory': 'true',
    //         'groupName': 'mandatory flag'
    //     }];


    constructor(
        private _matIcons: MatIconInfoService,
        private _translate: TranslateService,
        private _sharedService: SharedService) {
        this._matIcons.initIcons();
    }

    ngOnChanges(): void {
        if (this.assayType) {
            if (this.assayType === BasicAssayType.NIPTDPCR) {
                const lastProcessStepNameInfo = this._sharedService.getData('lastProcessStepNameInfo');
                if (lastProcessStepNameInfo) {
                    this.lastProcessStepName(lastProcessStepNameInfo.NIPTDPCR);
                }
            } else if (this.assayType === BasicAssayType.NIPTHTP) {
                const lastProcessStepNameInfo = this._sharedService.getData('lastProcessStepNameInfo');
                if (lastProcessStepNameInfo) {
                    this.lastProcessStepName(lastProcessStepNameInfo.NIPTHTP);
                }
            }
        }
    }

    lastProcessStepName(lastStepName) {
        if (this.processStepName === lastStepName) {
            if (this.fieldName) {
                const isRequiredInfo = this.getRequiredInfo();
                if (isRequiredInfo && isRequiredInfo.isMandatory === 'true') {
                    if (this.fieldValue === null || this.fieldValue === undefined || this.fieldValue === '') {
                        this.matIconInfo = IconsInfo.ERROR;
                    }
                }
            } else {
                if (this.isMandatory) {
                    this.matIconInfo = IconsInfo.ERROR;
                }
            }
        } else {
            if (this.fieldName) {
                const isRequiredInfo = this.getRequiredInfo();
                if (isRequiredInfo && isRequiredInfo.isMandatory === 'true') {
                    if (this.fieldValue === null || this.fieldValue === undefined || this.fieldValue === '') {
                        this.matIconInfo = IconsInfo.WARNING;
                    }
                } else {
                    this.matIconInfo = null;
                }
            } else {
                if (this.isMandatory) {
                    this.matIconInfo = IconsInfo.WARNING;
                } else {
                    this.matIconInfo = null;
                }
            }
        }
    }

    ngOnInit() {
        const translationInfo = this._translate.translations[this._translate.currentLang];
        this.missingPHI = translationInfo.orders.missingPHI;
    }

    getRequiredInfo() {
        if (this.requiredMandatoryInfo && this.requiredMandatoryInfo.length > 0) {
            return this.requiredMandatoryInfo.find(element => {
                if (element.fieldName === this.fieldName) {
                    return element;
                }
                // return {
                //     isMandatory: 'false'
                //  };
            }
        );
        } else {
            return {
                isMandatory: 'false'
             };
        }
    }
}
