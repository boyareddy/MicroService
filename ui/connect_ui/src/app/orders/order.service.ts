import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError } from 'rxjs';
import { CommonApiService } from '../shared/common-api.service';

@Injectable()
export class OrderService {

  // MOCK JSON Configs
  mockDataUri = './assets/JsonFiles/orders/';

  constructor(private _http: HttpClient, private _commonapiservice: CommonApiService) { }

  /* Validating the AccessioningId fetches from API */
  validateAccessioningId(accessioningId) {
    return this._commonapiservice.get('validateAccessioningId', accessioningId);
  }

  validateAccountNumber(accountNumber) {
    return this._commonapiservice.get('validateAccountNumber', accountNumber);
  }

  /* Is AccessioningId is CSV uploaded in DPCR level */
  validateCSVAccessioningId(accessioningId: any) {
    return this._commonapiservice.get('validateCSVAccessioningId', accessioningId);
  }

  /**
   * Get Inworkflow order count details
   */
  getUnassignedOrdersCount() {
    return this._commonapiservice.get('getUnassignedOrdersCount', undefined, true);
  }

  /**
   * Get Inworkflow order count details
   */
  getInworkflowOrdersCount() {
    return this._commonapiservice.get('getInWorkflowOrdersCount', undefined, true);
  }

  /**
   * getAssayTypes fetches the Assay Types
   */
  getAssayTypes() {
   return this._commonapiservice.get('getAssayType');
  }

  /**
   * getSampleTypes fetches the Sampe Types
   * @param assayTypeId
   */
  getSampleTypes(assayTypeId) {
   return this._commonapiservice.get('getSampleTypes', assayTypeId);
  }

  /**
   * getOrdersList fetches the Order List
   */
  getUnassignedOrdersList() {
   return this._commonapiservice.get('getUnassignedOrdersList', undefined, true);
  }


  /**
   * getWorkFlowOrdersList fetches the Order List
   */
  getInWorkflowOrdersList() {
   return this._commonapiservice.get('getInWorkflowOrdersList', undefined, true);
  }


  /**
   * getIVFStatus fetches the IVF Status
   */
  getIVFStatus(assayTypeId) {
   return this._commonapiservice.get('getIVFStatus', assayTypeId);
  }

  /**
   * getNumberOfFetuses fetches the Number Of Fetuses
   */
  getNumberOfFetuses(assayTypeId) {
   return this._commonapiservice.get('getNumberOfFetuses', assayTypeId);
  }

  /**
   * getEggDonner fetches the Egg Donner
   */
  getEggDonner(assayTypeId) {
   return this._commonapiservice.get('getEggDonner', assayTypeId);
  }

  /**
   *  Getting Eggdonor age based on assay type.
   */
  getEggDonorAge(assayTypeId) {
    return this._commonapiservice.get('getEggDonnerAge', assayTypeId);
    // return this._http.get('./assets/JsonFiles/orders/egg_donner_age.json');
  }

  /**
   * getTestOptions fetches the Test Options
   */
  getTestOptions(assayTypeId: number) {
   return this._commonapiservice.get('getTestOptions', assayTypeId);
  }

/**
   * getMaternalAge fetches the Maternal Age
   */
  getMaternalAge(assayTypeId) {
   return this._commonapiservice.get('getMaternalAge', assayTypeId);
  }

  /**
   * getGestationalAgeWeeks fetches the Gestational Age Weeks
   */
  onGettingGestationalAgeWeeksValues(assayTypeId) {
    return this._commonapiservice.get('getGestationalAgeWeeks', assayTypeId);
   }

   /**
   * getGestationalAgeDays fetches the Gestational Age Days
   */
  onGettingGestationalAgeDaysValues(assayTypeId) {
    return this._commonapiservice.get('getGestationalAgeDays', assayTypeId);
   }

  /**
   * Save the order
   * @param orderFormData OrderInformation
   */
  postOrders(orderFormData: any) {
    const headers = new HttpHeaders();
   headers.set('Content-Type', 'application/json');
    return this._commonapiservice.postNoPipe('postOrders', orderFormData);
  }

  /**
   * update orders updating order data.
   * @param updateFormDetails Updated Form Details
   */
  updateOrderDetails(updateFormDetails) {
    const headers = new HttpHeaders();
    headers.set('Content-Type', 'application/json');
    return this._commonapiservice.putNoPipe('updateOrderDetails', updateFormDetails);
  }

  /**
   * Getting order information for card details. Using order id.
   */
  getOrderInfo(orderId: any) {
    return this._commonapiservice.get('getOrderInfo', orderId);
  }

  /**
   * Getting workflow steps usind assay id.
   */
  getWorkFlowSteps(assayType: any) {
   return this._commonapiservice.get('getWorkFlowSteps', assayType);
  }

  /**
   * Getting workflow results usind order id.
   */
  getWorkFlowResults(accessioningId: any) {
   // tslint:disable-next-line:max-line-length
   return this._commonapiservice.get('getWorkFlowResults', accessioningId, true);
  }

  /*
  * Getting required fields information for given assay
  */
  getRequiredFieldsByAssay(assayType: any) {
    return this._commonapiservice.get('getRequiredFields', assayType);
  }

  /*
  * Getting process step for given accessioning id
  */
  getProcessStepByAccessioningId(accessioningId: any) {
    return this._commonapiservice.get('getProcessStepForAccessioningId', accessioningId);
  }

  /**
   * Getting the molicular ID for htp proccess by using plateType and plateLocation
   */
  getMolicularID(plateDetails: Object) {
    return this._commonapiservice.get('getMolicularID', plateDetails);
  }

  getBulkOrderTemplate() {
    return this._commonapiservice.getCSV('getBulkOrderOrderTemplate');
  }

  getDynamicBulkOrderemplate(browserLang) {
    return this._commonapiservice.getBulkOrderCSV('getBulkOrderOrderFromApiTemplate', browserLang);
  }

  createBulkOrders(bulkOrderJson) {
    return this._commonapiservice.post('createBulkOrders', bulkOrderJson);
  }

   /**
   * Updated the comments
   * */
  postComments(commentsJson: any) {
    return this._commonapiservice.put('updateComments', commentsJson);
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
