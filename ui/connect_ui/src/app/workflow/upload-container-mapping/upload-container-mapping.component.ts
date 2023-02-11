/**
 * Created on 10/11/2018
 * Created for User Story: RC-4153 and task: RC-4541
*/

import { Component, OnInit, Inject } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { SharedService } from '../../shared/shared.service';
import { WorkflowService } from '../workflow.service';
import { FileUploader } from 'ng2-file-upload';
import { Router } from '@angular/router';
import { ApiUrlService } from '../../shared/service-urls/apiurl.service';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';
import { processStepNames, getRunDetailsLableInfo } from '../workflow-dashboard.util';
import { STATUS_CODES } from '../model';

@Component({
  selector: 'app-upload-container-mapping',
  templateUrl: './upload-container-mapping.component.html',
  styleUrls: ['./upload-container-mapping.component.scss']
})
export class UploadContainerMappingComponent implements OnInit {

  headerInfo: HeaderInfo = {
    headerName: 'Upload container mapping',
    isCloseRequired: true,
    isCardsPage: true,
    navigateUrl: 'workflow'
  };
  uploader: FileUploader;
  hasBaseDropZoneOver = false;
  hasAnotherDropZoneOver = false;
  isUploadInProgress = false;
  isAnyUploadError = false;
  uploadErrors: any;
  apiProps: any;
  templateUrl: string = null;
  assayType: string;
  public assayTypeList: any;
  public assayTypeDefault = 'NIPTDPCR';

  constructor(private _workflowSvc: WorkflowService,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _snackbarService: SnackBarService,
    @Inject('UrlService') private _urlSvc: ApiUrlService,
    private _translate: TranslateService) { }

  ngOnInit() {

    // Get the api props.
    this.apiProps = this._sharedSvc.getData('appProperties');

    // Set the CSV config to uploader
    this.uploader = new FileUploader(this.apiProps.csvConfig);

    // calling the assay types
    // this.getAssayTypes();

    // Set the upload api.
    // tslint:disable-next-line:max-line-length
    this.uploader.setOptions(Object.assign(this.uploader, { url: `${this._urlSvc.urls.validateContainerMappedSamples}` }));

    // On success of upload.
    this.uploader.onSuccessItem = (item, response, status, headers) => this.onSuccessItem(item, response, status, headers);

    // On upload error.
    this.uploader.onErrorItem = (item, response, status, headers) => this.onErrorItem(item, response, status, headers);

    // On file adding error
    this.uploader.onWhenAddingFileFailed = (fileItem) => this.onWhenAddingFileFailed(fileItem);

    // Delete the tempUploadedMapping from shared service.
    this._sharedSvc.deleteData('tempUploadedMapping');
  }


  /**
   * Getting Basic Assay Types
   */
  public getAssayTypes () {
    this._workflowSvc.getAssayTypes().subscribe(response => {
      this.assayTypeList = response;
    }, error => {
      console.log(error);
      this._snackbarService.showErrorSnackBar('Error Occured while fetching the Assaytypes', SnackbarClasses.errorBottom1);
    });
  }

  /**
   * downloadContainerMappingTemplate() calls when user clicks the 'Download CSV template'.
  */
  donwloadConatinerMappingTemplate() {
    const urls = this._urlSvc.urls;
    // tslint:disable-next-line:max-line-length
    this.templateUrl = `${urls.getDynamicContainerMappingTemplate}/${urls.templateRoot}/${this.assayTypeDefault}/UploadContainerMapping/${urls.dynamicTemplate}`;

    this._workflowSvc.getDynamicContainerMappingTemplate(this.templateUrl).subscribe(successData => {
      this.parseCsvToDownload(successData);
    }, errorData => {
      console.log('Error', errorData);
      this._snackbarService.showErrorSnackBar('Error occured while downloading the template', SnackbarClasses.errorBottom1);
    });
  }

  /**
   * parseCsvToDownload parses the CSV format data and provide the link to download the CSV
   * @param data
   */
  parseCsvToDownload(data: any, csvName?: string) {
    const parsedResponse = data;
    const blob = new Blob([parsedResponse], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);

    if (navigator.msSaveOrOpenBlob) {
      navigator.msSaveBlob(blob, !csvName ? this.apiProps.contMapCSVFile : csvName);
    } else {
      const a = document.createElement('a');
      a.href = url;
      console.log(url, 'url name');
      a.download = !csvName ? this.apiProps.contMapCSVFile : csvName;
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

        // tslint:disable-next-line:max-line-length
        const tempUploadedMapping = Object.assign({}, { previewData: this.getFilterDataFromUpload(JSON.parse(response)), preQueryParams: { assayType: this.assayTypeDefault }, labelsInfo: this.getLabelsInfo(response) });
        this._sharedSvc.setData('tempUploadedMapping', tempUploadedMapping);
        this._router.navigate(['/workflow', 'containersamples', 'preview']);
    }, 3000);
  }

  public onErrorItem(item, response, status, headers) {
    setTimeout(timeOut => {
      // Hide upload progress.
      this.toggleUploadProgress(false);

      if (status === 0 || status === 404) {
        this.uploadErrors = this.getErrorMessage('errors.csvUploadConnLoss');
      } else {
        this.uploadErrors = JSON.parse(response);
      }
      this.isAnyUploadError = true;
    }, 3000);
  }

  public getErrorMessage(errorCode) {
    return {'errors.error': [errorCode]};
  }

  public onReTrying(event) {
    this.isAnyUploadError = false;
    this.isUploadInProgress = false;

    // Clear the upload queue.
    this.uploader.clearQueue();
  }

  public onWhenAddingFileFailed(fileItem) {
    console.log('fileItem', fileItem);
    if (this.uploader.options.allowedMimeType.indexOf(fileItem.type) === -1) {
      this.throwError({ 'Error :': ['Only CSV format is allowed'] });
    } else if (fileItem.size > this.uploader.options.maxFileSize) {
      // Codes for translation
      const browserLang = this._translate.currentLang;
      const messages = this._translate.translations[browserLang];

      // Translated error message with file size and unit.
      // tslint:disable-next-line:max-line-length
      const fileSizeError = messages.errors.csvMaxFileError.replace('$1', `${this.apiProps.csvConfig.displayFileSize}${messages.units[this.apiProps.csvConfig.fileSizeUnit]}`);

      this.throwError({ 'Error :': [fileSizeError] });
    }
  }

  public throwError(errorMsg: any) {
    this.isAnyUploadError = true;

    // Currently below is hard coded but later after getting confirmation will change to dynamic
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

  /**
   * To get the label image and label name by giving the assayType
   */
  getLabelsInfo(csvUploadResponse) {
    console.log(csvUploadResponse, 'cardResponse');
    let result: any;
    const stepName = processStepNames.NA_Extraction;
    const status = STATUS_CODES.COMPLETED;
    const assayType = csvUploadResponse[0].assayType ? csvUploadResponse[0].assayType : 'NIPTDPCR';
    if (assayType) {
      const { labelName, labelImage } = getRunDetailsLableInfo(stepName, status, assayType);
      result = {
        labelName: labelName,
        labelImage: labelImage
      };
    }
    return result;
  }

  public getFileSize(totalSize, unit) {
    switch (unit) {
      case('kb'):
        return totalSize / Math.pow(1024, 1);
      case('mb'):
        return totalSize / Math.pow(1024, 2);
      case('gb'):
        return totalSize / Math.pow(1024, 3);
    }
  }
}
