import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import { DeviceInfoComponent } from './device-info/device-info.component';

const routes: Routes = [
  {
    path: '',
    component: LandingComponent
  },
  {
    path: 'deviceInfo/:id',
    component: DeviceInfoComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SimulatorsRoutingModule { }
