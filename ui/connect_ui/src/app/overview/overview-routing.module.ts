import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LobbyComponent } from './lobby/lobby.component';
import { AuthGuardService } from '../auth/AuthService/auth-guard.service';
import { CommonModule } from '@angular/common';

const routes: Routes = [
  {
    path: '',
    component: LobbyComponent,
    canActivate: [AuthGuardService]
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class OverviewRoutingModule {}
