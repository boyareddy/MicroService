import { Component, OnInit, OnChanges, Input, ElementRef, ViewChild, Output, EventEmitter } from '@angular/core';
import { RequiredFieldValidationsForOrder,
  OrderSections,
  IconsInfo,
  patientInfo,
  assayInfo,
  orderDetailsInfo } from 'src/app/standard-names/constants';
import { MatIconInfoService } from 'src/app/shared/mat-icons/mat-icon-info.service';
import { SharedService } from 'src/app/shared/shared.service';
import { BasicAssayType } from 'src/app/workflow/workflow-dashboard.util';
import { WarningRequiredService } from 'src/app/shared/inline-htmls/warning-required.service';

@Component({
  selector: 'app-warning-required-order-info',
  templateUrl: './warning-required-order-info.component.html',
  styleUrls: ['./warning-required-order-info.component.scss']
})
export class WarningRequiredOrderInfoComponent implements OnChanges, OnInit {

  @Input() orderInfo: any;
  @Input() assayDetails: string;
  @Input() patientConfigInfo: any;
  @Input() stepName: string;
  @Input() processStepName: string;
  @ViewChild('warningInfo') warningInfo: ElementRef<any>;
  @Output() isWarningRequired: EventEmitter<any> = new EventEmitter();

  public patientFieldWarning = false;
  public dpcrLastStep: string;
  public htpLastStep: string;
  public svgIconInfo;

  public patientInfo = patientInfo;
  public assayInfo = assayInfo;
  public orderDetailsInfo = orderDetailsInfo;


  constructor(private _matIcons: MatIconInfoService,
    private _sharedService: SharedService,
    private _warningService: WarningRequiredService) {
    this._matIcons.initIcons();
   }

  ngOnChanges() {
    const lastStepName = this._sharedService.getData('lastProcessStepNameInfo');
    if (lastStepName) {
      this.dpcrLastStep = lastStepName.NIPTDPCR;
      this.htpLastStep = lastStepName.NIPTHTP;
    } else {
      this._warningService.getLastProcessStepName();
    }

    if (this.assayDetails === BasicAssayType.NIPTDPCR) {
      if (this.processStepName === this.dpcrLastStep) {
        this.svgIconInfo =  IconsInfo.ERROR_30;
      } else {
        this.svgIconInfo =  IconsInfo.WARNING_30;
      }
    } else if (this.assayDetails === BasicAssayType.NIPTHTP) {
      if (this.processStepName === this.htpLastStep) {
        this.svgIconInfo =  IconsInfo.ERROR_30;
      } else {
        this.svgIconInfo =  IconsInfo.WARNING_30;
      }
    }

    // console.log(this.orderInfo, this.patientConfigInfo);
    if (this.orderInfo) {
      if (this.stepName === OrderSections.PATIENT) {
        this.optionalRequiredWarningProcess(this.patientInfo);
      } else if (this.stepName === OrderSections.ASSAY) {
        this.optionalRequiredWarningProcess(this.assayInfo);
      } else if (this.stepName === OrderSections.ORDER) {
        this.optionalRequiredWarningProcess(this.orderDetailsInfo);
      }
    }
  }

  ngOnInit() {
  }

  optionalRequiredWarningProcess (stepInfo) {
    if (this.patientConfigInfo && this.patientConfigInfo.length > 0) {
      let isValid = false;
      stepInfo.forEach(ele => {
        this.patientConfigInfo.forEach(element => {
          if (ele.dbKey === element.fieldName) {
            const val = this.orderInfo[ele.orderKey];
            if (val === null || val === undefined || val === '') {
              isValid = true;
            }
            if (isValid) {
              return true;
            }
          }
        });
        this.patientFieldWarning = isValid;
        this.isWarningRequired.emit({
          stepName: this.stepName,
          isValid: this.patientFieldWarning
        });
        return isValid;
      });
    }
  }
}
