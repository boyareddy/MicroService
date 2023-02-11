import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from '../auth/AuthService/auth-guard.service';
import { UploadContainerMappingComponent } from './upload-container-mapping/upload-container-mapping.component';
import { RunDetailsComponent } from './view-run-htp/run-details/run-details.component';

import { ViewContainerMappingComponent } from './view-container-mapping/view-container-mapping.component';
import { ContainerMappedSamplesComponent } from './container-mapped-samples/container-mapped-samples.component';
import { PendingRunDetailsComponent } from './view-run-htp/pending-run-details/pending-run-details.component';
import { NewCardComponent } from './view-run-htp/new-card/new-card.component';
import { NewDashboardComponent } from './view-run-htp/new-dashboard/new-dashboard.component';
import { Ttv2analyzerComponent } from './ttv2analyzer/ttv2analyzer.component';


const workflowRoutes: Routes = [
  {
    path: '',
    component: NewDashboardComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'pendingruns',
    component: PendingRunDetailsComponent,
    canActivate: [AuthGuardService],
    data: {animation: 'slideLftRgt'}
  },
  {
    path: 'rundetails/:runId',
    component: RunDetailsComponent,
    canActivate: [AuthGuardService],
    data: {animation: 'slideLftRgt'}
  },
  {
    path: 'containersamples',
    component: UploadContainerMappingComponent,
    canActivate: [AuthGuardService],
    data: {animation: 'slideBtmTop'}
  },
  {
    path: 'mapped-samples',
    component: ContainerMappedSamplesComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'containersamples/preview',
    component: ViewContainerMappingComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: '',
    component: NewDashboardComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'new-dpcr',
    component: NewCardComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'ttv2analyzer',
    component: Ttv2analyzerComponent,
    canActivate: [AuthGuardService]
  }
  // {
  //   path: 'upload-container-mapping/:assay/:device',
  //   component: UploadContainerMappingComponent,
  //   canActivate: [AuthGuardService]
  // }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(workflowRoutes)
  ],
  exports: [RouterModule]
})
export class WorkflowRouterModule { }
