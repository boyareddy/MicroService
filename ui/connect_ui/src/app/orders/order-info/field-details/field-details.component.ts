import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-field-details',
  templateUrl: './field-details.component.html',
  styleUrls: ['./field-details.component.scss']
})
export class FieldDetailsComponent implements OnInit, OnChanges {

  @Input() labelName: string;
  @Input() labelValue: string;
  @Input() isDate = false;
  @Input() isOnlyDate = false;
  @Input() isOrderPage;
  @Input() processStepName: string;
  @Input() fieldName: string;
  @Input() patientConfigFile: any;
  @Input() assayType: string;
  public isMissingInfo = false;
  public missingText;
  constructor(private _translate: TranslateService) { }

  ngOnInit() {
    const translationInfo = this._translate.translations[this._translate.currentLang];
    this.missingText = translationInfo.orders.missing;
  }

  ngOnChanges() {
      if (this.patientConfigFile && this.patientConfigFile.length > 0) {
        this.patientConfigFile.find(element => {
          if (element.fieldName === this.fieldName) {
            this.isMissingInfo = true;
          }
        });
    } else {
      this.isMissingInfo = false;
    }
  }

}
