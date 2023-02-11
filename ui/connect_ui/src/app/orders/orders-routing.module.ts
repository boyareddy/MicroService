import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ManualOrderCreationComponent } from './order-creation/manual-order-creation/manual-order-creation.component';
import { AuthGuardService } from '../auth/AuthService/auth-guard.service';
import { OrderInfoComponent } from './order-info/order-info.component';
import { WorkflowmanagerOrdersComponent } from './workflowmanager-orders/workflowmanager-orders.component';
import { OrderCreationComponent } from './order-creation/order-creation.component';
import { BulkUploadPreviewComponent } from './order-creation/bulk-upload-preview/bulk-upload-preview.component';


const ordersRoutes: Routes = [
    {
        path: '',
        component:  WorkflowmanagerOrdersComponent ,
        canActivate: [AuthGuardService]
    },
    {
        path: 'create-order',
        component: OrderCreationComponent,
        canActivate: [AuthGuardService],
        data: {animation: 'slideBtmTop'}
    },
    {
        path: 'edit-order/:id/:step',
        component: ManualOrderCreationComponent,
        canActivate: [AuthGuardService],
        data: {animation: 'slideLftRgt1'}
    },
    {
        path: 'order-details/:id',
        component: OrderInfoComponent,
        canActivate: [AuthGuardService],
        data: {animation: 'slideLftRgt'}
    },
    {
        path: 'create-order/bulk-upload/preview',
        component: BulkUploadPreviewComponent,
        canActivate: [AuthGuardService],
        data: {animation: 'slideLftRgt'}
    }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(ordersRoutes)
  ],
  exports: [RouterModule]
})
export class OrdersRouterModule { }
