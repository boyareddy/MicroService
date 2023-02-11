import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

/**
 * Material Modules
*/
import {
  MatButtonModule,
  MatInputModule,
  MatCardModule,
  MatToolbarModule,
  MatIconModule,
  MatMenuModule,
  MatTabsModule,
  MatSelectModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatExpansionModule,
  MatSlideToggleModule,
  MatProgressBarModule,
  MatStepperModule,
  MatRadioModule,
  MatGridListModule,
  MatTableModule,
  MatSnackBarModule,
  MatDividerModule,
  MatDialogModule,
  MatTooltipModule,
  MatSortModule,
  MatProgressSpinnerModule,
} from '@angular/material';
/**
 * Angular Material Popover Module
 */
import { MdePopoverModule } from '@material-extended/mde';
import { SatPopoverModule } from '@ncstate/sat-popover';
import { TooltipModule } from 'ng2-tooltip-directive';


import 'hammerjs';


@NgModule({
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    MatTabsModule,
    MatSelectModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatSlideToggleModule,
    MatProgressBarModule,
    MatStepperModule,
    MatRadioModule,
    MatGridListModule,
    MatTableModule,
    MatSnackBarModule,
    MatDividerModule,
    MdePopoverModule,
    MatDialogModule,
    MatTooltipModule,
    MatSortModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    SatPopoverModule,
    TooltipModule
  ],
  exports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    MatTabsModule,
    MatSelectModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatSlideToggleModule,
    MatProgressBarModule,
    MatStepperModule,
    MatRadioModule,
    MatGridListModule,
    MatTableModule,
    MatSnackBarModule,
    MatDividerModule,
    MdePopoverModule,
    MatDialogModule,
    MatTooltipModule,
    MatSortModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    SatPopoverModule,
    TooltipModule
  ]
})
export class MaterialModule { }
