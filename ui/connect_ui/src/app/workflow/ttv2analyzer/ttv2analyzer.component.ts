import { Component, OnInit } from '@angular/core';
import { HeaderInfo } from 'src/app/shared/header.model';
import { DomSanitizer } from '@angular/platform-browser';
import { WorkflowService } from '../workflow.service';

@Component({
  selector: 'app-ttv2analyzer',
  templateUrl: './ttv2analyzer.component.html',
  styleUrls: ['./ttv2analyzer.component.scss']
})
export class Ttv2analyzerComponent implements OnInit {

  htmlContent: any = 'TTV2 analyzer';
  jobId = 3;
  public headerInfo: HeaderInfo = {
    headerName: 'Workflow manager',
    curPage: 'TTV2 Analyzer',
    headerIcon: 'assets/Images/workflow.png'
  };
  constructor(
    private domSanitizer: DomSanitizer,
    private _WS: WorkflowService
    ) { }

  ngOnInit() {
    this._WS.getTTV2AnalyzerContent(this.jobId).subscribe(
      (res: any) => {
        console.log(res, 'res===============');
        if (res) {
          this.htmlContent = this.domSanitizer.bypassSecurityTrustHtml(res);
          console.log(this.htmlContent, 'this.htmlContent');
        }
      },
      error => {
        console.log('getTTV2AnalyzerContent', error);
      }
    );
  }

}
