import { Injectable } from '@angular/core';
import { SharedService } from '../shared.service';

@Injectable()
export class ApiUrlService {

    apiProp = this.__sharedService.getData('appProperties');

    urls: any = {
        'validateBulkOrder': '',
        'validateContainerMappedSamples': '',
        'getDynamicContainerMappingTemplate': '',
        'templateRoot': '',
        'dynamicTemplate': ''
    };

    constructor(
        private __sharedService: SharedService
    ) {}

    setProperties() {
        this.apiProp = this.__sharedService.getData('appProperties');
        this.urls = {

            // Login page urls
            // tslint:disable-next-line:max-line-length
            'getPermissions': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.secApi.port}/${this.apiProp.secApi.module}/json/users/permissions`,
            // tslint:disable-next-line:max-line-length
            'validateAccessioningId': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/duplicate?accessioningID=queryParam`,

            // tslint:disable-next-line:max-line-length
            'validateAccountNumber': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/duplicate?accountNumber=queryParam`,

            // tslint:disable-next-line:max-line-length
            'getUserRoleInfo': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.secApi.port}/${this.apiProp.secApi.module}/json/users/userName/userNameDetails/domainName/domainDetails/roles`,

            // tslint:disable-next-line:max-line-length
            'validateCSVAccessioningId': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/containersamples?accessioningId=queryParam`,

            // tslint:disable-next-line:max-line-length
            'getAssayType': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay`,

            // tslint:disable-next-line:max-line-length
            'getSampleTypes': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/sampletype`,

            // tslint:disable-next-line:max-line-length
            'getUnassignedOrdersList' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order`,

            // tslint:disable-next-line:max-line-length
            'getUnassignedOrdersCount': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/unassignedcount`,

            // tslint:disable-next-line:max-line-length
            'getInWorkflowOrdersList' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/inworkflow`,

            // tslint:disable-next-line:max-line-length
            'getInWorkflowOrdersCount': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/inworkflowcount`,

            // tslint:disable-next-line:max-line-length
            'getIVFStatus' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/listdata?listType=ivf status`,

            // tslint:disable-next-line:max-line-length
            'getNumberOfFetuses' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/listdata?listType=Number of Fetus`,

            // tslint:disable-next-line:max-line-length
            'getEggDonner' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/listdata?listType=egg donor`,

            // tslint:disable-next-line:max-line-length
            'getEggDonnerAge' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/inputdatavalidations?fieldName=Egg Donor Age&groupName=data validation`,

            // tslint:disable-next-line:max-line-length
            'getGestationalAgeWeeks' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/inputdatavalidations?fieldName=Gestational Age Weeks&groupName=data validation`,

            // tslint:disable-next-line:max-line-length
            'getGestationalAgeDays' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/inputdatavalidations?fieldName=Gestational Age Days&groupName=data validation`,

            // tslint:disable-next-line:max-line-length
            'getTestOptions' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/testoptions`,

            // tslint:disable-next-line:max-line-length
            'getMaternalAge' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/inputdatavalidations?fieldName=Maternal Age&groupName=data validation`,

            // tslint:disable-next-line:max-line-length
            'getOrderInfo' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/queryParam`,

            // tslint:disable-next-line:max-line-length
            'getWorkFlowSteps' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/processstepaction`,

            // tslint:disable-next-line:max-line-length
            'getWorkFlowResults' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/processstepresults?accessioningId=queryParam`,

            // tslint:disable-next-line:max-line-length
            'getMolicularID' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/molecularIdDisplay?plateType=plateTypeVal&plateLocation=plateLocationVal`,

            // tslint:disable-next-line:max-line-length
            'postOrders' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order`,

            // tslint:disable-next-line:max-line-length
            'updateOrderDetails' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order`,

            // tslint:disable-next-line:max-line-length
            'getRequiredFields': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/queryParam/inputdatavalidations?groupName=mandatory flag`,

            // tslint:disable-next-line:max-line-length
            'getProcessStepForAccessioningId': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresult/inworkflow?accessioningId=queryParam`,
            /* Workflow API URL's */

            // tslint:disable-next-line:max-line-length
            'getRunInformation' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/assaytype/NIPTHTP?status=ongoing`,

            // tslint:disable-next-line:max-line-length
            'getRunInfoByWFS' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/list?assayType=assayTypeDetail&processstep=processStepVal&status=statusVal`,

             // tslint:disable-next-line:max-line-length
             'getRunInfoInCompletedByWFS' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/incompletedwfs?assayType=assayTypeDetail&processstep=processStepVal`,

            // tslint:disable-next-line:max-line-length
            'getRunInformationArchived' : `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/assaytype/queryParam?status=archived`,

            // tslint:disable-next-line:max-line-length
            'getContainerMappingTemplate': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.appPort}/${this.apiProp.contMapCSVPath}`,

            'getDynamicContainerMappingTemplate': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.appPort}`,

            'dynamicTemplate': `${this.apiProp.dynamicTemplate}`,

            'templateRoot': `${this.apiProp.csvTemplateRoot}`,

            // tslint:disable-next-line:max-line-length
            'getNAExtractionMappingDetails': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/containersamples/containeridlist?status=queryParam`,

           // tslint:disable-next-line:max-line-length
            'getContainerMappedSamples':  `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/containersamples?containerid=queryParam`,
            // tslint:disable-next-line:max-line-length
            'postContainerMappedSamples': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/containersamples`,

            // tslint:disable-next-line:max-line-length
            'validateContainerMappedSamples': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/containersamples/validate`,

             // tslint:disable-next-line:max-line-length
             'getHtpRunList': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/runresultsbyid/queryParam`,

             // tslint:disable-next-line:max-line-length
             'getSampleVolumes': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ammApi.port}/${this.apiProp.ammApi.module}/json/rest/api/v1/assay/assayTypeDetail/processstepaction?deviceType=deviceTypeID`,

             // tslint:disable-next-line:max-line-length
            'getBulkOrderOrderTemplate': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.appPort}/${this.apiProp.bulkOrdCSVPath}`,

            // tslint:disable-next-line:max-line-length
            'validateBulkOrder': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/validatebulkorders`,

            // tslint:disable-next-line:max-line-length
            'getBulkOrderOrderFromApiTemplate': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/getbulkorderstemplate/queryParam`,

            // tslint:disable-next-line:max-line-length
            'createBulkOrders': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/order/createbulkorders`,

            // tslint:disable-next-line:max-line-length
            'updateComments': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.rmmApi.port}/${this.apiProp.rmmApi.module}/json/rest/api/v1/runresults/updatecomments`,

            // tslint:disable-next-line:max-line-length
            'deleteSamples': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.ommApi.port}/${this.apiProp.ommApi.module}/json/rest/api/v1/containersamples/flag?containerId=queryParam`,

            // tslint:disable-next-line:max-line-length
            'getNotification': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.admmApi.port}/${this.apiProp.admmApi.module}/json/rest/api/v1/notification`,

            // tslint:disable-next-line:max-line-length
            'getNewNotification': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.admmApi.port}/${this.apiProp.admmApi.module}/json/rest/api/v1/notification/toaster`,

            // tslint:disable-next-line:max-line-length
            'upadateNotification': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.admmApi.port}/${this.apiProp.admmApi.module}/json/rest/api/v1/notification`,

            // tslint:disable-next-line:max-line-length
            'getAllDropdowns': `${this.apiProp.protocol}://${this.apiProp.host}:${this.apiProp.admmApi.port}/${this.apiProp.admmApi.module}/json/rest/api/v1/modules`,

            // tslint:disable-next-line:max-line-length
            'getTTV2AnalyzerContent': `${this.apiProp.protocol}://${this.apiProp.host}:83/api/analysis/iframe?jobId=queryParam`

        };
        console.log('==========================');
        console.log(this.apiProp, 'this.appProperties==========================');
        console.log('==========================');
      }
}

