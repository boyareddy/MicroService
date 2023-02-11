import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError } from 'rxjs';
import { CommonApiService } from '../shared/common-api.service';
import { SharedService } from '../shared/shared.service';
import { catchError } from 'rxjs/operators';

@Injectable()
export class WorkflowService {

  appProperties: any;
  constructor(private _http: HttpClient, private _commonapiservice: CommonApiService, private _sharedService: SharedService) {
    this.appProperties = this._sharedService.getData('appProperties');
  }

/**
   * Getting order information for card details. Using order id.
   */
  getRunInformation() {
    return this._commonapiservice.get('getRunInformation');
  }

  getRunInformationArchived(assayType) {
    return this._commonapiservice.get('getRunInformationArchived', assayType);
  }

  getContainerMappingTemplate() {
    return this._commonapiservice.getCSV('getContainerMappingTemplate');
  }

  getDynamicContainerMappingTemplate(templateUrl: string) {
    return this._commonapiservice.getCSVFromUrl(templateUrl);
  }

  getNAextractionInfo(status) {
    return this._commonapiservice.get('getNAExtractionMappingDetails', status, true);
  }

  getContainerMappedSamples(containerId) {
    return this._commonapiservice.get('getContainerMappedSamples', containerId);
  }

  postContainerMappedSamples(samples) {
    // return this._commonapiservice.post('postContainerMappedSamples', samples);
    return this._commonapiservice.postWithTextRes('postContainerMappedSamples', samples);
  }

  getHtpRunList(runId) {
    return this._commonapiservice.getNoPipe('getHtpRunList' , runId, true);
  }

  getSampleVolumes(assayType) {
    return this._commonapiservice.get('getSampleVolumes', assayType);
  }

  getWorkFlowSteps(assayType: string) {
    return this._commonapiservice.get('getWorkFlowSteps', assayType);
  }

  getRunInfoByWFS(queryParam) {
    return this._commonapiservice.get('getRunInfoByWFS', queryParam, true);
  }

  getRunInfoInCompletedByWFS(processStepName) {
    return this._commonapiservice.get('getRunInfoInCompletedByWFS', processStepName, true);
  }

  /**
   * Updated the comments
   * */
  postComments(commentsJson: any) {
    return this._commonapiservice.put('updateComments', commentsJson);
  }

  deleteContainerMappedSamples(containerId: string) {
    return this._commonapiservice.put('deleteSamples', containerId);
  }

  getTTV2AnalyzerContent(jobId) {
    return this._commonapiservice.getWithTextResponse('getTTV2AnalyzerContent', jobId);
  }

  /**
   * getAssayTypes fetches the Assay Types
   */
  getAssayTypes() {
    return this._commonapiservice.get('getAssayType');
   }

  public handleError(err: HttpErrorResponse) {
    let errMsg = '';
    if (err.error instanceof Error) {
      errMsg = err.error.message;
    } else {
      errMsg = err.error;
    }
    return throwError(errMsg);
  }
}

