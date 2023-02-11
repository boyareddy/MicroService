import { Component, OnInit } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { SharedService } from '../../shared/shared.service';
import { Router } from '@angular/router';
import { WorkflowService } from '../workflow.service';

@Component({
  selector: 'app-view-container-mapping',
  templateUrl: './view-container-mapping.component.html',
  styleUrls: ['./view-container-mapping.component.scss']
})
export class ViewContainerMappingComponent implements OnInit {

  headerInfo: HeaderInfo = {
    headerName: 'Upload container mapping',
    isCloseRequired: true,
    isCardsPage: false
  };

  uploadedContainer: any;
  containerIds: any = [];
  assayTypeInfo: string;
  public labelsInfo: any;

  constructor(private _sharedSvc: SharedService,
    private _router: Router,
    private _workflowSvc: WorkflowService,
    private _sharedService: SharedService) { }

  ngOnInit() {
    // Get the temporarily uploaded data.
    const tempUploadedMapping = this._sharedSvc.getData('tempUploadedMapping');

    if (tempUploadedMapping && tempUploadedMapping.previewData) {
      this.labelsInfo = tempUploadedMapping.labelsInfo;

      // Set assayType and devicetype
      this.assayTypeInfo = tempUploadedMapping.preQueryParams.assayTypeDefault;

      // Get all conatiner Ids
      this.containerIds = Object.keys(tempUploadedMapping.previewData.containers);
      console.log('containerids', this.containerIds);

      this.uploadedContainer = tempUploadedMapping.previewData;
    } else {
      this.onRedirectingToUpload();
    }
  }

  public onReTrying(event) {
    this.onRedirectingToUpload();
  }

  public onConfirming(event) {
    // Get the temporarily uploaded data.
    const tempUploadedMapping = this._sharedSvc.getData('tempUploadedMapping');

    const samples = this.filterToUpload(tempUploadedMapping.previewData.containers);
    this._workflowSvc.postContainerMappedSamples(samples).subscribe(successData => {
      this._sharedSvc.setData('csvUploadSuccess', successData);
      this._router.navigate(['/workflow']);
    }, errorData => {

    });
  }

  public onRedirectingToUpload() {
    this._router.navigate(['/workflow', 'containersamples']);
  }

  public filterToUpload(previewCSV) {
    const filterCSV = [];
    if (previewCSV) {
      // Get container IDs
      const containerIds = Object.keys(previewCSV);

      containerIds.forEach(containerId => {
        filterCSV.push(previewCSV[containerId]);
      });
    }
    return [].concat.apply([], filterCSV);
  }
}
