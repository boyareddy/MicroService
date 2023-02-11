import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RsrLoginRoutingModule } from './rsr-login-routing.module';
import { RsrComponent } from './rsr/rsr.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    RsrLoginRoutingModule
  ],
  declarations: [
    RsrComponent
  ]
})
export class RsrLoginModule { }
