import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RsrComponent } from './rsr/rsr.component';
import { AuthGuardService } from '../auth/AuthService/auth-guard.service';

const routes: Routes = [
  {
    path: '',
    component: RsrComponent,
    canActivate: [AuthGuardService]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RsrLoginRoutingModule { }
