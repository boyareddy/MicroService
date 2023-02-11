import { Component, OnInit, Sanitizer } from '@angular/core';
import { DmService } from '../services/dm.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-jasper-report',
  templateUrl: './jasper-report.component.html',
  styleUrls: ['./jasper-report.component.scss']
})
export class JasperReportComponent implements OnInit {

  pdf: any;
  langs: any[] = [{lang: 'English ( US )', code: 'en-US'}, {lang: 'Français', code: 'fr-FR'}];
  lang: string = "en/US"

  constructor(private _dmSvc: DmService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
  }

  exportPDF(){
    this._dmSvc.exportReport(this.lang).subscribe(pdfData => {
      console.log(pdfData);
      var fileURL = URL.createObjectURL(pdfData);
      window.open(fileURL);
    }, error => {
      console.log("Error occured");
    });
  }

  downloadPDF(){
    this._dmSvc.exportReport(this.lang).subscribe(pdfData => {
      console.log(pdfData);
      this.save(pdfData, 'jasper-report');
    }, error => {
      console.log("Error occured");
    });
  }

  save(_pdf: Blob, expCsvFileNm: string){
    //let _csv: string = toCSV(_json);
    let expCsvFile: string = expCsvFileNm + '.pdf' || 'export.pdf';

    //let _blob: Blob = new Blob([_pdf], {type: 'text/csv;charset=utf-8'});
    
    if(navigator.msSaveBlob){
        navigator.msSaveBlob(_pdf, expCsvFile);
    }else{
        let _link: any = document.createElement('a');
        if(_link.download !== undefined){
            let _url: any = URL.createObjectURL(_pdf);
            _link.setAttribute('href', _url);
            _link.setAttribute('download', expCsvFile);
            _link.style.visibility = 'hidden';
            document.body.appendChild(_link);
            _link.click();
            document.body.removeChild(_link);
        }
    }
  }

  openPdfInNewWindow(){
    this._dmSvc.exportReport(this.lang).subscribe(pdfData => {
      let pdfUrl = URL.createObjectURL(pdfData);
      this.pdf = this.sanitizer.bypassSecurityTrustResourceUrl(pdfUrl);
    }, error => {
      console.log("Error occured");
    });
  }

  onLangChange(event){
    this.lang = event.target.value !== 'null' ? event.target.value.split("-").join("/") : 'en/US'; 
    console.log("Lang", this.lang);
  }

}
