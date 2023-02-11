import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { WorkflowService } from '../../workflow.service';
import * as moment from 'moment-timezone';
import { ActivatedRoute } from '@angular/router';


@Component({
    selector: 'app-run-archieved',
    templateUrl: './run-archieved.component.html',
    styleUrls: ['./run-archieved.component.scss']
})
export class RunAchievedComponent implements OnInit, OnChanges {
    @Input() runList;
    @Input() workFlowSteps;
    @Input() runArchiveDetailsRes = [];
    runArchiveDetails;
    displayedColumns: string[] = ['runID', 'workflowStep', 'sampleCount', 'user', 'completedTime', 'symbol' ];
    runResArr = [];
    trans_ob;

    constructor(
        private translate: TranslateService
    ) {}

    ngOnInit() {
    }

    ngOnChanges() {
        this.trans_ob = this.translate.translations[this.translate.currentLang];
        //this.createJSONFormat();
        console.log("runArchiveDetailsRes", this.runArchiveDetailsRes);
    }

    createJSONFormat() {
        this.runArchiveDetails = [];
        if (this.runArchiveDetailsRes) {
            this.runArchiveDetailsRes.forEach((value, ind) => {
                ind = this.runArchiveDetails.length;
                if (ind > 0) {
                    const noOfWeek = this.getWeekDetails(value.runCompletionTime);
                    let isIndAval =  null;
                    this.runArchiveDetails.forEach( (val, index) => {
                        if (val.weekDetails === noOfWeek) {
                            isIndAval = index;
                        }
                    });
                    if (isIndAval != null) {
                        // this.addRunDetails(true, isIndAval, value);
                        const runListLength = this.runArchiveDetails[isIndAval]['runList'].length;
                        this.runArchiveDetails[isIndAval]['runList'][runListLength] = value;
                    } else {
                        this.runArchiveDetails[ind] = {};
                        this.runArchiveDetails[ind]['runList'] = [];
                        const runListLength = this.runArchiveDetails[ind]['runList'].length;
                        this.runArchiveDetails[ind]['weekDetails'] = noOfWeek;
                        const weekNumber = moment(value.runCompletionTime).week();
                        this.runArchiveDetails[ind]['weekNo'] = weekNumber;
                        this.runArchiveDetails[ind]['runList'][runListLength] = value;
                    // this.addRunDetails(false, runArrLen, value);
                    }
                } else {
                    this.runArchiveDetails[ind] = {};
                    this.runArchiveDetails[ind]['runList'] = [];
                    const runListLength = this.runArchiveDetails[ind]['runList'].length;
                    this.runArchiveDetails[ind]['weekDetails'] = this.getWeekDetails(value.runCompletionTime);
                    const weekNumber = moment(value.runCompletionTime).week();
                    this.runArchiveDetails[ind]['weekNo'] = weekNumber;
                    this.runArchiveDetails[ind]['runList'][runListLength] = value;
                }
                console.log(this.runArchiveDetails, '>>>>> this.runArchiveDetails');
            });
           // tslint:disable-next-line:max-line-length
           this.runArchiveDetails.sort((a, b) => (a.weekDetails.split()[0] + a.weekNo) > (b.weekDetails.split()[0] + b.weekNo) ? -1 : (a.weekDetails.split()[0] + a.weekNo) < (b.weekDetails.split()[0] + b.weekNo) ? 1 : 0);
        }
    }

    getWeekDetails(date) {
        if (!Number.isNaN(date) && date !== null && date !== undefined && date !== '') {
            console.log(date, 'date ----- ');
            let weekDetails = null;
            const weekNumber = moment(date).week();
            const startOfWeekStr = moment(date).startOf('week').toString();
            const endOfWeekStr = moment(date).endOf('week').toString();
            const startOfWeek = new Date(startOfWeekStr).getDate();
            const endOfWeek = new Date(endOfWeekStr).getDate();
            const monthOfWeek_1 = moment(startOfWeekStr).format('MMM');
            const monthOfWeek_2 = moment(endOfWeekStr).format('MMM');
            // tslint:disable-next-line:max-line-length
            weekDetails = moment(endOfWeekStr).format('YYYY') + ' ' + this.getTranslation('Week') + ' ' + weekNumber + ': ' + this.getTranslation(monthOfWeek_1) + ' ' + startOfWeek + ' - ' + this.getTranslation(monthOfWeek_2) + ' ' + endOfWeek;
            return weekDetails;
        } else {
            return '';
        }
    }

    getTranslation(trans_str) {
        console.log(trans_str, 'this.trans_ob8888888888888888');
        // tslint:disable-next-line:max-line-length
        const tr_str = (this.trans_ob['calender'] ? this.trans_ob['calender'] : null) ? (this.trans_ob['calender'][trans_str.toUpperCase()] ? this.trans_ob['calender'][trans_str.toUpperCase()] : trans_str) : trans_str;
        return tr_str;
    }

}
