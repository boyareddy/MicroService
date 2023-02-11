
import { Component, OnInit, Inject } from '@angular/core';
import { HeaderInfo } from '../../../shared/header.model';
import { SharedService } from '../../../shared/shared.service';
import { FileUploader, ParsedResponseHeaders } from 'ng2-file-upload';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material';
import { ApiUrlService } from '../../../shared/service-urls/apiurl.service';
import { OrderService } from '../../order.service';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';
import moment from 'moment-timezone';
import { TranslateService } from '../../../../../node_modules/@ngx-translate/core';

@Component({
  selector: 'app-bulk-order-upload',
  templateUrl: './bulk-order-upload.component.html',
  styleUrls: ['./bulk-order-upload.component.scss']
})
export class BulkOrderUploadComponent implements OnInit {

  uploader: FileUploader;
  hasBaseDropZoneOver = false;
  hasAnotherDropZoneOver = false;
  isUploadInProgress = false;
  isAnyUploadError = false;
  uploadErrors: any;
  apiProps: any;
  templateUrl: string = null;
  assayType: string;
  deviceType: string;

  constructor(private _commonApisvc: OrderService,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _acRoute: ActivatedRoute,
    private _snackBar: MatSnackBar,
    private _snackBarSvc: SnackBarService,
    private _translate: TranslateService,
    @Inject('UrlService') private _urlSvc: ApiUrlService) { }

  ngOnInit() {
    // Get the api props.
    this.apiProps = this._sharedSvc.getData('appProperties');

    // Set the CSV config to uploader
    this.uploader = new FileUploader(this.apiProps.bulkOrderCSVConfig);

    const timeZone = moment.tz.guess();
    const currentTime = new Date();
    const timeZoneOffset = currentTime.getTimezoneOffset();
    const timeZoneFormat = moment.tz.zone(timeZone).abbr(timeZoneOffset);

    // Set the upload api.
    this.uploader.setOptions(Object.assign(this.uploader, {
      url: `${this._urlSvc.urls.validateBulkOrder}?tz=${timeZoneFormat}`,
     // headers: [{ 'timeZone': timeZoneOffset}]
    }));

    // On success of upload.
    this.uploader.onSuccessItem = (item, response, status, headers) => this.onSuccessItem(item, response, status, headers);

    // On upload error.
    this.uploader.onErrorItem = (item, response, status, headers) => this.onErrorItem(item, response, status, headers);

    // On file adding error
    this.uploader.onWhenAddingFileFailed = (fileItem) => this.onWhenAddingFileFailed(fileItem);

    // Delete the tempUploadedMapping from shared service.
    this._sharedSvc.deleteData('tempBulkOrderUpload');
  }

  /**
   * donwloadConatinerMappingTemplate() calls when user clicks the 'Download CSV template'.
  */
  donwloadConatinerMappingTemplate() {
    const translations = this._translate.translations[this._translate.currentLang];
    console.log('test123');
    this._commonApisvc.getBulkOrderTemplate().subscribe(successData => {
      console.log(successData);
      this.parseCsvToDownload(successData);
    }, errorData => {
      const message1 = `${translations.orders.notificationmsg.errorDownloadTemplate}`;
        this._snackBarSvc.showErrorSnackBar(`${message1}`, SnackbarClasses.errorBottom1);
    });
    // const browserLang = window.navigator.language;
    // console.log(browserLang, 'browserLang**********');
    // this._commonApisvc.getDynamicBulkOrderemplate(browserLang).subscribe(successData => {
    //   this.parseCsvToDownload(successData);
    // }, errorData1 => {
    //   console.log('Error', errorData1.status);
    //   // Hardcoding the message.Later will change to dynamic.
    //   // tslint:disable-next-line:max-line-length
    //   if (errorData1.status === 0 || errorData1.status === 404) {
    //     this._snackBarSvc.showErrorSnackBar(`Error occurred. Kindy re-try upload.`, SnackbarClasses.errorBottom2);
    //   } else {
    //     this._snackBarSvc.showErrorSnackBar(`Error occured while downloading the template.`, SnackbarClasses.errorBottom1);
    //   }
    // });
  }

  /**
   * parseCsvToDownload parses the CSV format data and provide the link to doanload the CSV
   * @param data
   */
  parseCsvToDownload(data: any, csvName?: string) {
    const parsedResponse = data;
    const blob = new Blob([parsedResponse], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);

    if (navigator.msSaveOrOpenBlob) {
      navigator.msSaveBlob(blob, !csvName ? this.apiProps.bulkOrdCSVFile : csvName);
    } else {
      const a = document.createElement('a');
      a.href = url;
      a.download = !csvName ? this.apiProps.bulkOrdCSVFile : csvName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    }
    window.URL.revokeObjectURL(url);
  }

  fileOverAnother(e: any): void {
    this.hasAnotherDropZoneOver = e;
  }

  onDrop(event) {
    console.log('onDrop', event);
    this.onFileUpload(this.uploader);
  }

  public fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  public onFileOver(event): void {

  }

  public onFileUpload(uploader) {
    // Display upload progress.
    this.toggleUploadProgress(true);

    if (uploader.queue.length > 0) {
      uploader.queue[0].alias = 'myfile';
      uploader.queue[0].upload();
    }
  }

  public onSuccessItem(item, response, status, headers) {
    setTimeout(timeOut => {
      // Hide upload progress.
      this.toggleUploadProgress(false);
      this._sharedSvc.setData('tempBulkOrderUpload', response);
      this._router.navigate(['/orders/create-order/bulk-upload/preview']);
    }, 3000);
  }

  public onErrorItem(item, response, status, headers) {
    const translations = this._translate.translations[this._translate.currentLang];
    setTimeout(timeOut => {
      // Hide upload progress.
      this.toggleUploadProgress(false);
      console.log(status, 'status********');
      // tslint:disable-next-line:curly
      if (status === 400) {
        this.uploadErrors = JSON.parse(response);
        // tslint:disable-next-line:max-line-length
      } else {
        const message1 = `${translations.orders.notificationmsg.errorBulkUpload}`;
        this.uploadErrors = ['Error occurred. Kindy re-try upload.'];
        this._snackBarSvc.showErrorSnackBar(`${message1}`, SnackbarClasses.errorBottom2);
      }
      this.isAnyUploadError = true;
    }, 3000);
  }


  public onReTrying(event) {
    this.isAnyUploadError = false;
    this.isUploadInProgress = false;

    // Clear the upload queue.
    this.uploader.clearQueue();
  }

  public onWhenAddingFileFailed(fileItem) {
    if (this.uploader.options.allowedMimeType.indexOf(fileItem.type) === -1) {
      this.throwError(['Invalid file format.']);
    } else if (fileItem.size > this.uploader.options.maxFileSize) {
      this.throwError(['File exceeds allowed size of 1 MB.']);
    }
  }

  public throwError(errorMsg: any) {
    this.isAnyUploadError = true;

    // Currently below is hardcoded but later after getting confirmation will change to dynamic
    this.uploadErrors = errorMsg;
  }

  public toggleUploadProgress(toggle: boolean) {
    this.isUploadInProgress = toggle;
  }

  public getFilterDataFromUpload(csvUploadResponse) {
    const result = { sampleCount: null, containers: [] };
    if (csvUploadResponse) {
      result.sampleCount = csvUploadResponse.length;
      csvUploadResponse.filter(element => {
        if (!result.containers[element.containerID]) {
          result.containers[element.containerID] = [element];
        } else {
          result.containers[element.containerID].push(element);
        }
      });
    }
    console.log('getFilterDataFromUpload', result);
    return result;
  }
}

