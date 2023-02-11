/**
 * Angular Core
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule} from '@angular/forms';

/**
 * Order Info
 */
import { AssayDetailsComponent } from './order-info/assay-details/assay-details.component';
import { OrderDetailsComponent } from './order-info/order-details/order-details.component';
import { PatientDetailsComponent } from './order-info/patient-details/patient-details.component';
import { WorkFlowComponent } from './order-info/work-flow/work-flow.component';
import { OrderInfoComponent } from './order-info/order-info.component';

/**
 * Order Creation
 */
import { ManualOrderCreationComponent } from './order-creation/manual-order-creation/manual-order-creation.component';


/**
 * Order List
 */
import { OrderListComponent } from './order-list/order-list.component';

/**
 * Order Service
 */
import { OrderService } from './order.service';
import { SharedModule } from '../shared/shared.module';
import { OrdersUnassignedComponent } from './orders-unassigned/orders-unassigned.component';
import { OrdersRouterModule } from './orders-routing.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpRequestInterceptorService } from '../shared/http-request-interceptor.service';
import { OrdersInworkflowComponent } from './orders-inworkflow/orders-inworkflow.component';
import { WorkflowmanagerOrdersComponent } from './workflowmanager-orders/workflowmanager-orders.component';
import { FooterComponent } from './footer/footer.component';
import { CommonApiService } from '../shared/common-api.service';
import { NumberOnlyDirective } from '../shared/directives/numbers-only.directive';
import { BulkOrderUploadComponent } from './order-creation/bulk-order-upload/bulk-order-upload.component';
import { OrderCreationComponent } from './order-creation/order-creation.component';
import { FileUploadModule } from 'ng2-file-upload';
import { BulkUploadPreviewComponent } from './order-creation/bulk-upload-preview/bulk-upload-preview.component';
import { FieldDetailsComponent } from './order-info/field-details/field-details.component';
import { SharedOrderDetailsComponent } from './order-info/shared-order-details/shared-order-details.component';
import { OngoingComponent } from './order-info/work-flow/ongoing/ongoing.component';
import { NotOngoingComponent } from './order-info/work-flow/not-ongoing/not-ongoing.component';
import { WarningRequiredOrderInfoComponent } from './warning-required-order-info/warning-required-order-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    OrdersRouterModule,
    FileUploadModule
  ],
  declarations: [
    AssayDetailsComponent,
    OrderDetailsComponent,
    PatientDetailsComponent,
    WorkFlowComponent,
    ManualOrderCreationComponent,
    OrderListComponent,
    OrdersUnassignedComponent,
    OrdersInworkflowComponent,
    WorkflowmanagerOrdersComponent,
    OrderInfoComponent,
    FooterComponent,
    NumberOnlyDirective,
    BulkOrderUploadComponent,
    OrderCreationComponent,
    BulkUploadPreviewComponent,
    FieldDetailsComponent,
    SharedOrderDetailsComponent,
    OngoingComponent,
    NotOngoingComponent,
    WarningRequiredOrderInfoComponent
  ],
  providers: [
    OrderService,
    CommonApiService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptorService,
      multi: true
    }
  ]
})
export class OrdersModule { }
