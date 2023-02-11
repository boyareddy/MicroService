import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SimulatorsRoutingModule } from './simulators-routing.module';
import { LandingComponent } from './landing/landing.component';
import { DeviceInfoComponent } from './device-info/device-info.component';
import { MaterialModules } from '../material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SimulatorsRoutingModule,
    MaterialModules,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [LandingComponent, DeviceInfoComponent]
})
export class SimulatorsModule { }
