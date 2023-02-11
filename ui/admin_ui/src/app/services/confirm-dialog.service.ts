import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDialogService {

  constructor(private _dialogBox: MatDialog, private translateSvc: TranslateService) { }

  public openWarningDialog(): void{
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    this._dialogBox.open(ConfirmDialogComponent, {
      width: '486px',
      height: '200px',
      hasBackdrop: true,
      //backdropClass: 'progress-loader-no-bd',
      data: {onlyWarn: translations.common.noChanges ? translations.common.noChanges : 'common.noChanges'},
    });
  }

  public openConfirmDialog(): MatDialogRef<ConfirmDialogComponent>{
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    return this._dialogBox.open(ConfirmDialogComponent, {
      width: '486px',
      height: '185px',
      hasBackdrop: true,
      //backdropClass: 'progress-loader-no-bd',
      data: translations.common.saveChanges ? translations.common.saveChanges : 'common.saveChanges' 
    })
  }

  public openConfirmDialogParam(param: string, action: string): MatDialogRef<ConfirmDialogComponent>{
    return this._dialogBox.open(ConfirmDialogComponent, {
      width: '486px',
      height: '185px',
      //backdropClass: 'progress-loader-no-bd',
      hasBackdrop: true,
      data: {param, action}
    })
  }
}
