import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SharedService } from '../shared.service';

@Injectable({
    providedIn: 'root'
})

export class WarningRequiredService {
    constructor(private _http: HttpClient, private _sharedSvc: SharedService) { }


    getLastProcessStepName() {
        const properties = this._sharedSvc.getData('appProperties');

        if (properties) {
            const { protocol, host, ammApi: { port, module } } = this._sharedSvc.getData('appProperties');
            const url = `${protocol}://${host}:${port}/${module}/json/rest/api/v1/assay/processstepaction`;
            const lastProcessStepNameInfo = this._sharedSvc.getData('lastProcessStepNameInfo');

            if (!lastProcessStepNameInfo) {
                this._http.get(url).subscribe(response => {
                    const responseData: any = response;
                    const data = {};
                    responseData.forEach(element => {
                        data[element.assayType] = element.processStepName;
                    });
                    this._sharedSvc.setData('lastProcessStepNameInfo', data);
                }, error => {
                    console.error('error while fetching process stepName', error);
                });
            }
        }
    }
}
