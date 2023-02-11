import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { CustomMessageSnackbarComponent } from '../shared/custom-message-snackbar/custom-message-snackbar.component';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private _snackBar: MatSnackBar, private _translateSvc: TranslateService) { }

  /**
   * Display the snackbar
   * @param message Message Info
   * @param className Class [Either success-snackbar-bottom1/ success-snackbar-bottom2]
   */
  public showSuccessSnackBar(message: string, className: string): void {
    this._snackBar.openFromComponent(CustomMessageSnackbarComponent,  {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className, 'text-wrap']
    });
  }

  /**
   * Display the snackbar
   * @param message Message Info
   * @param className Class [Either failed-snackbar-bottom1/ failed-snackbar-bottom2]
   */
  public showErrorSnackBar(message: string, className: string): void {
    this._snackBar.openFromComponent(CustomMessageSnackbarComponent, {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className, 'text-wrap']
    });
  }

  public showSuccessSnackBarWithLocalize(messageCode: string, className: string): void {
    let message = this.getMessage(messageCode);
    this._snackBar.openFromComponent(CustomMessageSnackbarComponent,  {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className, 'text-wrap']
    });
  }

  public showSuccessSnackBarWithLocalizeAndReplace(messageCode: string,replaceFor: string, replaceWith: string, className: string): void {
    let message = this.getMessage(messageCode).replace(replaceFor, replaceWith);
    this._snackBar.openFromComponent(CustomMessageSnackbarComponent,  {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className, 'text-wrap']
    });
  }

  public showErrorSnackBarWithLocalize(messageCode: string, className: string){
    let message = this.getMessage(messageCode)
    this._snackBar.openFromComponent(CustomMessageSnackbarComponent, {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className, 'text-wrap']
    });
  }

  public getMessage(messageCode: string){
    let currentLang = this._translateSvc.currentLang;
    let keys = messageCode.split('.');
    let message = this._translateSvc.translations[currentLang][keys[0]][keys[1]];
    return message;
  }

  public getSingleMessage(messageCode: string){
    let currentLang = this._translateSvc.currentLang;
    let message = this._translateSvc.translations[currentLang][messageCode];
    return message;
  }
}
