import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { WorkflowRouterModule } from './workflow-router.module';
import { FileUploadModule } from 'ng2-file-upload';
import { CommonApiService } from '../shared/common-api.service';
import { WorkflowService } from './workflow.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptorService } from '../shared/http-request-interceptor.service';
import { UploadContainerMappingComponent } from './upload-container-mapping/upload-container-mapping.component';
import { SampleListComponent } from './view-run-htp/run-details/sample-list/sample-list.component';
import { SampleRunCardComponent } from './view-run-htp/run-details/sample-run-card/sample-run-card.component';
import { RunDetailsComponent } from './view-run-htp/run-details/run-details.component';
import { ContainerMappingFooterComponent } from './container-mapping-footer/container-mapping-footer.component';
import { MappedContainerComponent } from './mapped-container/mapped-container.component';
import { ViewContainerMappingComponent } from './view-container-mapping/view-container-mapping.component';
import { RunAchievedComponent } from './view-run-htp/run-archieved/run-archieved.component';
import { UploadErrorComponent } from './upload-error/upload-error.component';
import { OngoingProgressbarComponent } from './view-run-htp/run-details/ongoing-progressbar/ongoing-progressbar.component';
import { ContainerMappedSamplesComponent } from './container-mapped-samples/container-mapped-samples.component';
import { PendingRunDetailsComponent } from './view-run-htp/pending-run-details/pending-run-details.component';
import { RunArchievdTableComponent } from './view-run-htp/run-archievd-table/run-archievd-table.component';
import { NewCardComponent } from './view-run-htp/new-card/new-card.component';
import { NewDashboardComponent } from './view-run-htp/new-dashboard/new-dashboard.component';
import { SingleRowDetailsComponent } from './view-run-htp/run-details/single-row-details/single-row-details.component';
import { Ttv2analyzerComponent } from './ttv2analyzer/ttv2analyzer.component';

@NgModule({
    imports: [
      CommonModule,
      SharedModule,
      ReactiveFormsModule,
      WorkflowRouterModule,
      FileUploadModule
    ],
    declarations: [
        UploadContainerMappingComponent,
        SampleListComponent,
        RunDetailsComponent,
        SampleRunCardComponent,
        ContainerMappedSamplesComponent,
        ContainerMappingFooterComponent,
        MappedContainerComponent,
        ViewContainerMappingComponent,
        RunAchievedComponent,
        UploadErrorComponent,
        OngoingProgressbarComponent,
        PendingRunDetailsComponent,
        RunArchievdTableComponent,
        NewCardComponent,
        NewDashboardComponent,
        SingleRowDetailsComponent,
        Ttv2analyzerComponent
    ],
    providers: [
        WorkflowService,
        CommonApiService,
        {
        provide: HTTP_INTERCEPTORS,
        useClass: HttpRequestInterceptorService,
        multi: true
        }
    ]
  })
  export class WorkflowModule { }
