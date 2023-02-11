
import { Injectable } from '@angular/core';
import { SharedService } from '../shared.service';

@Injectable()
export class MockUrlService {
    mockDataUrl = './assets/JsonFiles/';
    apiProp;
    urls = {
        // Login page urls
        'getPermissions': `${ this.mockDataUrl }permissions.json`,
        'getUserRoleInfo': `${ this.mockDataUrl }permissions.json`,
        // Order creation page URL's
        'getAssayType': `${ this.mockDataUrl }orders/assay_types.json`,
        'getSampleTypes': `${ this.mockDataUrl }orders/sample_types.json`,
        'postOrders': `${ this.mockDataUrl }orders/singleorder.json`,
        'updateOrderDetails': `${ this.mockDataUrl }orders/singleorder.json`,
        'getIVFStatus': `${ this.mockDataUrl }orders/ivf_status.json`,
        'getNumberOfFetuses': `${ this.mockDataUrl }orders/fetuses.json`,
        'getEggDonner': `${ this.mockDataUrl }orders/egg_donner.json`,
        'getEggDonnerAge': `${ this.mockDataUrl }orders/egg_donner_age.json`,
        'getTestOptions': `${ this.mockDataUrl }orders/testoptions.json`,
        'getMaternalAge': `${ this.mockDataUrl }orders/maternal_age.json`,
        'getUnassignedOrdersCount': `${ this.mockDataUrl }orders/ordersCount.json`,
        'getInWorkflowOrdersCount': `${ this.mockDataUrl }orders/ordersCount.json`,
        // Workflow orders managers page URL's
        'getUnassignedOrdersList': `${ this.mockDataUrl }orders/unassigned_listing_order.json`,
        'getInWorkflowOrdersList': `${ this.mockDataUrl }orders/inworkflow_listing_order.json`,
        // Order details page URL's
        'getOrderInfo': `${ this.mockDataUrl }orders/singleorder.json`,
        // Workflow Order details page URL's
        'getWorkFlowSteps': `${ this.mockDataUrl }orders/workflowstep.json`,
        'getWorkFlowResults': `${ this.mockDataUrl }orders/workflowresult.json`,
        'getMolicularID': `${ this.mockDataUrl }orders/molicularId.json`,
        /**
         * Workflow manager URLs
         */
        // Run results ongoing and completed page
        'getHtpRunList' : `${this.mockDataUrl }workflow/htprun-list.json`,
        'getSampleVolumes' : `${this.mockDataUrl }workflow/getSampleVolumes.json`,
        'getRunInformation' : `${this.mockDataUrl }workflow/run-ongoing.json`,
        'getRunInfoByWFS' : `${this.mockDataUrl }workflow/run-ongoing-WFS.json`,
        'getRunInfoInCompletedByWFS' : `${this.mockDataUrl }workflow/run-completed-WFS.json`,
        'getRunInformationArchived': `${this.mockDataUrl}workflow/run-archieved.json`,
        'getContainerMappingTemplate': `${this.mockDataUrl }workflow/cont-map.csv`,
        'getNAExtractionMappingDetails': `${this.mockDataUrl}workflow/naExtractionMapping.json`,
        'getContainerMappedSamples': `${this.mockDataUrl}workflow/containerMappedSamplesNA.json`,


        /* Dash board services */
        'getNAExtractionCompleted' : `${this.mockDataUrl}workflow/NACompleted.json`,
        'getLibraryPrepOngoing': `${this.mockDataUrl}workflow/libraryprepOngoing.json`,
        'getLibraryPrepCompleted': `${this.mockDataUrl}workflow/libraryprepCompleted.json`,

        // tslint:disable-next-line:max-line-length
       'getBulkOrderOrderTemplate': `${ this.mockDataUrl }workflow/getSampleVolumes.json`,
        // 'getBulkOrderOrderTemplate': `${ this.mockDataUrl }workflow/Bulk upload - order entry - NIPT-dPCR.csv`
        'getNotification': `${ this.mockDataUrl }orders/notification.json`,
        'getNewNotification': `${ this.mockDataUrl }orders/newnotification.json`,
        'getAllDropdowns': `${this.mockDataUrl}orders/dropdownvalues.json`,
        'getTTV2AnalyzerContent': `${this.mockDataUrl}workflow/TTV2AnalyzerContent.json`
    };
    constructor(
        private __sharedService: SharedService
    ) {}
    setProperties() {
        this.apiProp = this.__sharedService.getData('appProperties');
    }
}
